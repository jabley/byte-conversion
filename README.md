JMH Benchmark around cost of byte->char conversion.

I discovered some code that was reading an `InputStream` to a `StringBuilder` (byte->char conversion), then back to bytes `StringBuilder#toString#getBytes`, then back to chars.

This seemed quite inefficient.

This repo demonstrates using JMH to create a simple benchmark around the overhead in doing these multiple conversions. This gives us data to back up our intuition that converting between bytes and chars is unnecessary cost.

## Results

```
# Run complete. Total time: 00:13:34

Benchmark                                Mode  Cnt      Score     Error  Units
MyBenchmark.testByte2Char2Byte          thrpt  200   8782.366 ±  64.105  ops/s
MyBenchmark.withoutNeedlessConversions  thrpt  200  18697.557 ± 163.237  ops/s
```
