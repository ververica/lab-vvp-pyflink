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

import sys
import logging

from pyflink.table import (TableEnvironment, EnvironmentSettings)

import my_udfs


def main():
    t_env = TableEnvironment.create(EnvironmentSettings.in_streaming_mode())

    t_env.create_temporary_function("PY_UPPER", my_udfs.py_upper)

    # define the source
    my_table = t_env.from_elements(
        elements=[
            (1, 'Berlin'),
            (2, 'Shanghai'),
            (3, 'New York'),
            (4, 'Hangzhou')
        ],
        schema=['id', 'city'])

    t_env.sql_query("SELECT id, PY_UPPER(city) city FROM %s" % my_table) \
        .execute() \
        .print()


if __name__ == '__main__':
    logging.basicConfig(stream=sys.stdout, level=logging.INFO, format="%(message)s")

    main()

