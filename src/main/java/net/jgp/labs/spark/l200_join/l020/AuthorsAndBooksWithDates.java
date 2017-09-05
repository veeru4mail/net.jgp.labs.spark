package net.jgp.labs.spark.l200_join.l020;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class AuthorsAndBooksWithDates {

  public static void main(String[] args) {
    AuthorsAndBooksWithDates app = new AuthorsAndBooksWithDates();
    app.start();
  }

  private void start() {
    SparkSession spark = SparkSession.builder().appName("Authors and Books").master(
        "local").getOrCreate();

    String filename = "data/authors.csv";
    // @formatter:off
    Dataset<Row> authorsDf = spark.read()
        .format("csv")
        .option("inferSchema", "true")
        .option("header", "true")
        .option("dateFormat", "mm/dd/yy")
        .load(filename);
    // @formatter:on
    authorsDf.show();

    filename = "data/books.csv";
    // @formatter:off
    Dataset<Row> booksDf = spark.read()
        .format("csv")
        .option("inferSchema", "true")
        .option("header", "true")
        .load(filename);
    // @formatter:on
    booksDf.show();

    // @formatter:off
    Dataset<Row> libraryDf = authorsDf
        .join(booksDf, authorsDf.col("id").equalTo(booksDf.col("authorId")), "full_outer")
        .withColumn("bookId", booksDf.col("id"))
        .drop(booksDf.col("id"));
    // @formatter:on
    libraryDf.show();
    libraryDf.printSchema();

  }
}
