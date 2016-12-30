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

  private static final String LARGE_INT_STRING = "11111111";

  private static final String LARGE_LONG_STRING = "111111111111111";

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
  public int parseIntSmallJdk() {
    return Integer.parseInt("1");
  }

  @Benchmark
  public int parseIntSmallExact() {
    return CharSequences.parseIntExact("1");
  }

  @Benchmark
  public int parseIntLarge() {
    return CharSequences.parseInt(LARGE_INT_STRING);
  }

  @Benchmark
  public int parseIntLargeExact() {
    return CharSequences.parseIntExact(LARGE_INT_STRING);
  }

  @Benchmark
  public int parseIntLargeJdk() {
    return Integer.parseInt(LARGE_INT_STRING);
  }

  @Benchmark
  public long parseLongSmall() {
    return CharSequences.parseLong("1");
  }

  @Benchmark
  public long parseLongSmallExact() {
    return CharSequences.parseLongExact("1");
  }

  @Benchmark
  public long parseLongSmallJdk() {
    return Long.parseLong("1");
  }

  @Benchmark
  public long parseLongLarge() {
    return CharSequences.parseLong(LARGE_LONG_STRING);
  }

  @Benchmark
  public long parseLongLargeExact() {
    return CharSequences.parseLongExact(LARGE_LONG_STRING);
  }

  @Benchmark
  public long parseLongLargeJdk() {
    return Long.parseLong(LARGE_LONG_STRING);
  }

}
