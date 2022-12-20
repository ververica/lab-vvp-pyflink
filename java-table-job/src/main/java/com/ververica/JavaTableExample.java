/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ververica;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.util.Arrays;

/**
 * A simple example for demonstrating the use of Python UDFs in a Java-based Table API program.
 * This example is developed based on
 * <a href="https://github.com/apache/flink/blob/master/flink-examples/flink-examples-table/src/main/java/org/apache/flink/table/examples/java/basics/StreamSQLExample.java">StreamSQLExample</a> in Apache Flink.
 */
public final class JavaTableExample {

    // *************************************************************************
    //     PROGRAM
    // *************************************************************************

    public static void main(String[] args) throws Exception {

        // Set up the Java DataStream API
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Set up the Java Table API
        final StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        // Set the python.files setting
        tableEnv.getConfig().getConfiguration().setString("python.files", "/flink/usrlib/my_udfs.py");

        // Define the temporary function
        tableEnv.executeSql("create temporary system function PY_UPPER as 'my_udfs.py_upper' language python");

        final DataStream<Order> order =
                env.fromCollection(
                        Arrays.asList(
                                new Order(1L, "beer", 3),
                                new Order(2L, "apple", 4)
                        )
                );

        // Convert the DataStream to a Table object, it will be used "inline" and is not registered in a catalog
        final Table table = tableEnv.fromDataStream(order);

        // Call UDF PY_UPPER() in Flink SQL
        tableEnv.sqlQuery(
                "SELECT user, PY_UPPER(product) product, amount FROM " + table)
            .execute()
            .print();
    }

    // *************************************************************************
    //     USER DATA TYPES
    // *************************************************************************

    /** Simple POJO. */
    public static class Order {
        public Long user;
        public String product;
        public int amount;

        // for POJO detection in DataStream API
        public Order() {}

        // for structured type detection in Table API
        public Order(Long user, String product, int amount) {
            this.user = user;
            this.product = product;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return "Order{"
                    + "user="
                    + user
                    + ", product='"
                    + product
                    + '\''
                    + ", amount="
                    + amount
                    + '}';
        }
    }
}

