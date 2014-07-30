package ivmaykov.com.github

object ScalaJsonBenchmarksApp extends App {
  val providers: List[JsonProvider] = List(
    new ArgonautProvider,
    new JacksonProvider,
    new Json4sJacksonProvider,
    new Json4sNativeProvider,
    new SprayJsonProvider).sortBy { x => scala.math.random }

  val mapSizes = Vector(100, 300, 600, 1000, 3000, 6000, 10000, 30000, 60000, 100000, 300000, 600000)
  val dataVector = generateDataVector(mapSizes)

  Console.println("Warming up the JVM .... ")

  // Do a partial run through without recording results, to warm up the JVM somewhat
  for (provider <- providers) {
    for (data <- dataVector if data.size < 60000) {
      val serialized = provider.serializeMapToJson(data)
      val deserialized = provider.deserializeMapFromJson(serialized)
    }
  }

  val results = new scala.collection.mutable.HashMap[(JsonProvider, Int), (Double, Double, Double, Double)]()

  Console.println("===== Raw data =====")
  Console.println("provider, data size, serialize time (ms), deserialize time (ms), serialize keys/ms, deserialize keys/ms")

  for (provider <- providers) {
    for (data <- dataVector) {
      // Note: spray-json's deserialization performance plummets with a large number of keys, so we don't
      // bother benchmarking it past 60k keys
      if (!(provider.isInstanceOf[SprayJsonProvider] && data.size > 60000)) {
        val serializeTimes = new scala.collection.mutable.ArrayBuffer[Long]()
        val deserializeTimes = new scala.collection.mutable.ArrayBuffer[Long]()
        // Take median-of-three
        for (i <- 0 until 3) {
          val startNanos = System.nanoTime()
          val serialized = provider.serializeMapToJson(data)
          val serializedNanos = System.nanoTime()
          val deserialized = provider.deserializeMapFromJson(serialized)
          val endNanos = System.nanoTime()
          serializeTimes.append(serializedNanos - startNanos)
          deserializeTimes.append(endNanos - serializedNanos)
        }
        val serMs = serializeTimes.sorted.apply(serializeTimes.size / 2).toDouble / 1000000
        val deserMs = deserializeTimes.sorted.apply(deserializeTimes.size / 2).toDouble / 1000000
        val serKeysPerMs = data.size.toDouble / serMs
        val deserKeysPerMs = data.size.toDouble / deserMs
        val row = List[Any](provider.name, data.size, serMs, deserMs, serKeysPerMs, deserKeysPerMs).mkString(",")
        Console.println(row)
        results((provider, data.size)) = (serMs, deserMs, serKeysPerMs, deserKeysPerMs)
      }
    }
  }

  val x = results.groupBy { case (k, v) => k._2 }

  Console.println("===== Deserialization speed (keys / ms) =====")
  Console.println((List("data size") ++ providers.map { _.name }).mkString(", "))
  mapSizes.foreach { s =>
    Console.print(s + ", ")
    Console.println(providers.map { p => x(s).get((p,s)).map { _._4 }.getOrElse(0.0) }.mkString(", "))
  }

  Console.println("===== Serialization speed (keys / ms) =====")
  Console.println((List("data size") ++ providers.map { _.name }).mkString(", "))
  mapSizes.foreach { s =>
    Console.print(s + ", ")
    Console.println(providers.map { p => x(s).get((p,s)).map { _._3 }.getOrElse(0.0) }.mkString(", "))
  }

  def generateDataVector(mapSizes: Seq[Int]): Seq[Map[String, Seq[Long]]] = {
    val random = new java.util.Random()
    mapSizes.map { size =>
      (0 until size).map { i =>
        i.toString -> Vector.fill(5)(random.nextLong())
      }.toMap
    }
  }
}
