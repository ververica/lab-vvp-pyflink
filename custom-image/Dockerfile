################################################################################
#  Licensed to the Apache Software Foundation (ASF) under one
#  or more contributor license agreements.  See the NOTICE file
#  distributed with this work for additional information
#  regarding copyright ownership.  The ASF licenses this file
#  to you under the Apache License, Version 2.0 (the
#  "License"); you may not use this file except in compliance
#  with the License.  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
# limitations under the License.
################################################################################

# A custom image like this is only needed for Ververica Platform 2.8 or earlier.
# Ververica Platform 2.9 or later comes with Python and PyFlink out of box.

FROM registry.ververica.com/v2.8/flink:1.15.2-stream4-scala_2.12-java11

USER root:root

RUN apt update \
    && apt -y install python3.8 python3-pip \
    && python3.8 -m pip install apache-flink==1.15.2 \
    && apt clean \
    && ln -s /usr/bin/python3.8 /usr/bin/python \
    && rm -rf /var/lib/apt/lists/*

USER flink:flink
