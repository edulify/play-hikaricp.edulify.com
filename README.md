# HikariCP Plugin for Play Framework 2.2.x

This plugins is supposed to work with version `2.2.x` of PlayFramework. It uses version `1.3.8` of HikariCP.

## Why HikariCP?

[HikariCP](https://github.com/brettwooldridge/HikariCP) is supposed to be the fastest connection pool in Java land. But we did not start to use it because of speed, but because of its reliability. After suffering with connection leaks from BoneCP, we decide to implement our own database plugin to replace the default one. You can see [a discussion about database exceptions](https://groups.google.com/forum/#!topic/play-framework/7PBnjiXkNuU) caused by BoneCP (or misconfiguration of it). Also there are numerous other discussions about people having problems related to BoneCP.

Here is how HikariCP is working for us:

![HikariCP in Production](http://i.imgur.com/xPqkc7T.png)

## Versions

| Version | HikariCP | Play  | Comment                          |
|--------:|---------:|------:|:---------------------------------|
| 1.3.0   | 1.3.8    | 2.3.1 | Upadates Play and Scala versions |
| 1.2.0   | 1.3.8    | 2.2.3 | Supports Heroku like services    |
| 1.1.0   | 1.3.8    | 2.2.3 | Updates HikariCP and Play        |
| 1.0.0   | 1.3.5    | 2.2.2 | First stable release             |

## Repository

You need to add the following repository in order to use this module:

```scala
resolvers += Resolver.url("Edulify Repository", url("http://edulify.github.io/modules/releases/"))(Resolver.ivyStylePatterns)
```

## How to Use

There are just a few steps to properly configure the plugin. Just follow the steps bellow:

### Step 1: Add dependencies

Add the following dependency to your `project/build.sbt` or `project/Build.scala`:

    "com.edulify" %% "play-hirakicp" % "1.3.0"

### Step 2: Disable default `dbplugin`

Add the following line to your `conf/application.conf`:

    dbplugin=disabled

This will disable dbplugin and avoids that BoneCP creates useless connections (which in some cases can create database problems, like exhaust available connections).

### Step 3: Enable HikariCP Plugin

Add the following line to your `conf/play.plugins`:

    1500:com.edulify.play.hikaricp.HikariCPPlugin

### Step 4: Configure HikariCP

##### Using `db.default.hikaricp.file`

**That is the preferred way to configure HikariCP** because you have full access to all [properties documented here](https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby) and you can also have specific configuration to development, test and production modes. You can create a specific hikari properties file and configure it using `db.default.hikaricp.file` in you `conf/application.conf` file.

Per instance, if you have a `conf/production.conf` that is loaded by play in production mode, add the following line to this file:

     db.default.hikaricp.file="conf/hikaricp.prod.properties"

Of course, you need to create `conf/hikaricp.dev.properties` file.

##### Using `hikaricp.properties`

Just create a `conf/hikaricp.properties` and the plugin will read it and create DataSource. **This mode has preference over using ordinary play way** because you have fine grained access to Hikari configuration.

##### Using ordinary [Play way](http://www.playframework.com/documentation/2.2.x/SettingsJDBC)

This the least recommended way. Configure database properties like stated by Play docs. The table bellow shows how Play configurations are mapped to HikariCP:

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

## Deploying to Heroku

When using Heroku, you need to [read database url string](https://devcenter.heroku.com/articles/heroku-postgresql#connecting-in-java) from an environment variable called `DATABASE_URL`. Plain java Properties does not offer a way to reference these environment variables in a properties file, then we use [Commons Configuration](http://commons.apache.org/proper/commons-configuration/) to read the `hikaricp.properties` file or the one configured by `db.default.hikaricp.file`.

Here is an example:

```
jdbcUrl=${env:DATABASE_URL}
driverClassName=org.postgresql.Driver

connectionTestQuery=SELECT 1
registerMbeans=true

# 15 minutes
maxLifetime=900000
# 5 minutes
idleTimeout=300000

maximumPoolSize=20
minimumIdle=5
```

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
