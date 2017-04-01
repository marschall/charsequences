package com.github.marschall.charsequences;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ParseUuidBenchmark {

  private static final String TEST_UUID = "ba226cf7-d156-4b18-a78a-094736208cc9";

  public static void main(String[] args) throws RunnerException {
    Options options = new OptionsBuilder()
            .include(".*ParseUuidBenchmark.*")
            .warmupIterations(10)
            .measurementIterations(10)
            .forks(10)
            .build();
    new Runner(options).run();
  }

  @Benchmark
  public UUID parseCharSequences() {
    return CharSequences.uuidFromCharSequence(TEST_UUID);
  }

  @Benchmark
  public UUID parseJdk() {
    return UUID.fromString(TEST_UUID);
  }

}
