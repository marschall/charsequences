package com.github.marschall.charsequences;

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
public class ParseDecimalBenchmark {

  public static void main(String[] args) throws RunnerException {
    Options options = new OptionsBuilder()
            .include(".*ParseDecimalBenchmark.*")
            .warmupIterations(10)
            .measurementIterations(10)
            .forks(10)
            .build();
    new Runner(options).run();
  }

  @Benchmark
  public int parseIntSmall() {
    return CharSequences.parseInt("1");
  }

  @Benchmark
  public int parseIntLarge() {
    return CharSequences.parseInt("11111111");
  }

  @Benchmark
  public int parseIntExactSmall() {
    return CharSequences.parseIntExact("1");
  }

  @Benchmark
  public int parseIntExactLarge() {
    return CharSequences.parseIntExact("11111111");
  }

  @Benchmark
  public long parseLongSmall() {
    return CharSequences.parseLong("1");
  }

  @Benchmark
  public long parseLongLarge() {
    return CharSequences.parseLong("11111111");
  }

  @Benchmark
  public long parseLongExactSmall() {
    return CharSequences.parseLongExact("1");
  }

  @Benchmark
  public long parseLongExactLarge() {
    return CharSequences.parseLongExact("11111111");
  }

}
