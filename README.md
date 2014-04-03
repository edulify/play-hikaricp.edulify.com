# HikariCP Plugin for Play Framework 2.2.x

This plugins is supposed to work with version `2.2.x` of PlayFramework. It uses version `1.3.4` of HikariCP.

## Why HikariCP?

[HikariCP](https://github.com/brettwooldridge/HikariCP) is supposed to be the fastest connection pool in Java land. But we did not start to use it because of speed, but because of its reliability. After suffering with connection leaks from BoneCP, we decide to implement our own database plugin to replace the default one. You can see [a discussion about database exceptions](https://groups.google.com/forum/#!topic/play-framework/7PBnjiXkNuU) caused by BoneCP (or misconfiguration of it). Also there are numerous other discussions about people having problems related to BoneCP.

## How to Use

### Add dependencies

### Disable default `dbplugin`

### Enable HikariCP Plugin

### Configure HikariCP

##### Using `hikari.properties`

##### Using ordinary [Play way](http://www.playframework.com/documentation/2.2.x/SettingsJDBC)

The table bellow shows how Play configurations are mapped to HikariCP:

Hikari                                          | Play                           | Defaults
:-----------------------------------------------|:-------------------------------|:-----------
`driverClassName`                               | `db.default.driver`            | -
`jdbcUrl`                                       | `db.default.url`               | -
`username`                                      | `db.default.user`              | -
`password`                                      | `db.default.password`          | -
 -                                              | `db.default.partitionSize`     | -
`maximumPoolSize` (partitionSize * maxPoolSize) | `db.default.maxPoolSize`       | -
`minimumPoolSize` (partitionSize * minPoolSize) | `db.default.minPoolSize`       | -
`maxLifetime`                                   | `db.default.maxConnectionAge`  | -
`readOnly`                                      | `db.default.defaultReadOnly`   | `false`
`acquireRetryDelay`                             | `db.default.acquireRetryDelay` | -
`registerMbeans`                                | `db.default.statisticsEnabled` | `false`
`connectionInitSql`                             | `db.default.initSQL`           | -

## Inspirations and Alternatives

The code here is highly inspired by the following plugins:

1. [swaldman/c3p0-play](https://github.com/swaldman/c3p0-play)
2. [autma/play-hikaricp-plugin](https://github.com/autma/play-hikaricp-plugin)

We decide to do our own because both plugins above looks unmaintained.

There are also two other alternatives using c3p0:

1. [hadashi/play2-c3p0-plugin](https://github.com/hadashi/play2-c3p0-plugin)
2. [Furyu/play-c3p0-plugin](https://github.com/Furyu/play-c3p0-plugin)

## License

Copyright 2014 Edulify.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.