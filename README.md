# HikariCP Plugin for 2.3.x

This plugin works with `2.3.x` of PlayFramework and uses version `2.3.2` of HikariCP.

Note, it can be made to work with Play `2.2.x` of the PlayFramework, but it requires changing the dependencies as the current build relies on the Play `2.3.x` plugin. Please, see the versions table below to see which version of Play is supported.

[![Build Status](https://travis-ci.org/edulify/play-hikaricp.edulify.com.svg?branch=master)](https://travis-ci.org/edulify/play-hikaricp.edulify.com) [![Issue Stats](http://issuestats.com/github/edulify/play-hikaricp.edulify.com/badge/pr)](http://issuestats.com/github/edulify/play-hikaricp.edulify.com) [![Dependency Status](https://www.versioneye.com/user/projects/54eaa687d1ec577c97000cd5/badge.svg?style=flat)](https://www.versioneye.com/user/projects/54eaa687d1ec577c97000cd5) [![Coverage Status](https://coveralls.io/repos/edulify/play-hikaricp.edulify.com/badge.svg?branch=master)](https://coveralls.io/r/edulify/play-hikaricp.edulify.com?branch=master)

## About this documentation

When reading this documentation at Github, select the tag that matches the version of the plugin that you are using.

## Why HikariCP?

[HikariCP](https://github.com/brettwooldridge/HikariCP) is supposed to be the fastest connection pool in Java land. But we did not start to use it because of speed, but because of its reliability. After suffering with connection leaks from BoneCP, we decide to implement our own database plugin to replace the default one. You can see [a discussion about database exceptions](https://groups.google.com/forum/#!topic/play-framework/7PBnjiXkNuU) caused by BoneCP (or misconfiguration of it). Also there are numerous other discussions about people having problems related to BoneCP.

Here is how HikariCP is working for us:

![HikariCP in Production](http://i.imgur.com/xPqkc7T.png)

## Versions

| Version | HikariCP | Play  |
|--------:|---------:|------:|
| 2.0.0   | 2.3.2    | 2.3.8 |
| 1.5.2   | 2.3.2    | 2.3.8 |
| 1.5.1   | 2.2.5    | 2.3.7 |
| 1.5.0   | 2.0.1    | 2.3.4 |
| 1.4.1   | 2.0.1    | 2.3.2 |
| 1.4.0   | 1.4.0    | 2.3.1 |
| 1.3.1   | 1.3.8    | 2.3.1 |
| 1.3.0   | 1.3.8    | 2.3.1 |
| 1.2.0   | 1.3.8    | 2.2.3 |
| 1.1.0   | 1.3.8    | 2.2.3 |
| 1.0.0   | 1.3.5    | 2.2.2 |

For more information about what changed in each version, please, see the our CHANGELOG.md file.

## Repository

You need to add the following repository in order to use this module:

```scala
resolvers += Resolver.url("Edulify Repository", url("http://edulify.github.io/modules/releases/"))(Resolver.ivyStylePatterns)
```

## How to Use

There are just a few steps to properly configure the plugin:

### Step 1: Add the dependency

Add the following dependency to your `project/build.sbt` or `project/Build.scala`:

    "com.edulify" %% "play-hikaricp" % "2.0.0"

### Step 2: Disable default `dbplugin`

Add the following line to your `conf/application.conf`:

    dbplugin=disabled

This will disable dbplugin and avoids that BoneCP creates useless connections (which in some cases can create database problems, like exhaust available connections).

### Step 3: Enable HikariCP Plugin

Add the following line to your `conf/play.plugins`:

    200:com.edulify.play.hikaricp.HikariCPPlugin

Due to the fact that the [Play JPA plugin](https://github.com/playframework/playframework/blob/master/framework/src/play-java-jpa/src/main/resources/play.plugins) is assigned a priority of **400**, please make sure that you assign `com.edulify.play.hikaricp.HikariCPPlugin` a priority less than that when using datasources looked up via JNDI. Otherwise, during application startup when JPA attempts to create the `EntityManagerFactory` your data source will not have been bound to JNDI yet. [Play documentation](http://playframework.com/documentation/2.3.x/ScalaPlugins) states that connection pools should use a **200** priority.

### Step 4: Configure HikariCP

Please, before reading this section, we recommend that you get familiarized with [HikariCP configurations](https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby). It is the main source to understand each possible configuration for HikariCP and it have invaluable information about how to best configure your pool. Also, it is a good idea to read about [Typesafe Config](https://github.com/typesafehub/config), which is the configuration language used by PlayFramework.

This plugin just read each possible Hikari configuration and create the pool from it and all the defaults are honored. Here is a simple example of how to configure you pool in `application.conf`:

    db {
      default {
        driverClassName=org.postgresql.Driver
        jdbcUrl="jdbc:mysql://localhost:3306/simpsons"
        username=bart
        password=51mp50n
      }
    }

Alternatively, you can configure it using a data source provided by your JDBC Driver:

    db {
      default {
        dataSourceClassName=org.postgresql.ds.PGSimpleDataSource
        dataSource {
          user=bart
          password=51mp50n
          databaseName=springfield
          serverName=localhost
        }
      }
    }

HikariCP documentation has a [list of JDBC DataSource classes for popular databases](https://github.com/brettwooldridge/HikariCP#popular-datasource-class-names). Both `dataSourceClassName` and `jdbcUrl` are supported, but HikariCP recommends the former. Also, you can configure more than one database:

    db {

      default {
        dataSourceClassName=org.postgresql.ds.PGSimpleDataSource
        dataSource {
          user=bart
          password=51mp50n
          databaseName=springfield
          serverName=localhost
        }
      }

      orders {
        dataSourceClassName=org.postgresql.ds.PGSimpleDataSource
          dataSource {
            user=homer
            password=duffbeer
            databaseName=orders
            serverName=localhost
          }
        }
    }

## Log SQL statements

HikariCP does not offer (out of the box) a way to log SQL statements and it recommends that you use the log capacities of your database vendor. From HikariCP docs:

> **Log Statement Text / Slow Query Logging**
>
> Like Statement caching, most major database vendors support statement logging through properties of their own driver. This includes Oracle, MySQL, Derby, MSSQL, and others. Some even support slow query logging. We consider this a "development-time" feature. For those few databases that do not support it, jdbcdslog-exp is a good option. Great stuff during development and pre-Production.

We take the suggestion of using [jdbcdslog-exp](https://code.google.com/p/jdbcdslog-exp/) and have implemented SQL log statement support, which can be configured by database, using `logSql` property:

    db {
      default {

        logSql=true

        dataSourceClassName=org.postgresql.ds.PGSimpleDataSource
        dataSource {
          user=bart
          password=51mp50n
          databaseName=springfield
          serverName=localhost

        }
      }
    }

After that, you can configure the jdbcdslog-exp [log level as explained in their manual](https://code.google.com/p/jdbcdslog/wiki/UserGuide#Setup_logging_engine). Basically, you need to configure your root logger to `INFO` and then decide what jdbcdslog-exp will log (connections, statements and result sets). Here is an example using `conf/application.conf` to configure the logs:

    logger.root=INFO
    logger.org.jdbcdslog.ConnectionLogger=ERROR # won't log connections
    logger.org.jdbcdslog.StatementLogger=INFO   # log all statements
    logger.org.jdbcdslog.ResultSetLogger=ERROR  # won't log result sets

## JNDI Support

Thanks to contributions from the community, this plugin supports to bind a DataSource to a JNDI context. Here is an example of how to add configure it in `conf/application.conf`:

    db {
      default {

        jndiName="DefaultDataSource"

        dataSourceClassName=org.postgresql.ds.PGSimpleDataSource
        dataSource {
          user=bart
          password=51mp50n
          databaseName=springfield
          serverName=localhost
        }
      }
    }

## Deploying to Heroku

When using Heroku, you first need to translate its `DATABASE_URL` into another variable with the format expected by PostgreSQL JDBC Driver. As stated by [Heroku docs](https://devcenter.heroku.com/articles/heroku-postgresql#spring-java):

> The `DATABASE_URL` for the Heroku Postgres add-on follows this naming convention:
>
>     postgres://<username>:<password>@<host>/<dbname>
>
> However the Postgres JDBC driver uses the following convention:
>
>     jdbc:postgresql://<host>:<port>/<dbname>?user=<username>&password=<password>
>
> Notice the additional `ql` at the end of `jdbc:postgresql`? Due to this difference you will need to hardcode the scheme to jdbc:postgresql` in your Java class or your Spring XML configuration.

So, you will need to create another environment variable with the proper format and you will also need to create variables for username and password. To do this, run the following commands:

      $ heroku config:set DATABASE_JDBC_URL="jdbc:postgresql://host:5432/dbname"
      $ heroku config:set DATABASE_USERNAME=username
      $ heroku config:set DATABASE_PASSWORD=password

You can obtain the correct values for `host`, `dbname`, `username`, and `password` from the `DATABASE_URL` environment variable, which Heroku creates for you. Then you can use these variables directly inside `application.conf`, since it is supported by [Typesafe Config](https://github.com/typesafehub/config):

    db {
      default {
        driverClassName=org.postgresql.Driver
        jdbcUrl=${DATABASE_JDBC_URL}
        username=${DATABASE_USERNAME}
        password=${DATABASE_PASSWORD}
      }
    }

## Contributors

You can see a full list of contributors [here](https://github.com/edulify/play-hikaricp.edulify.com/graphs/contributors). Thank you very much to all the people that helps us to maintain and evolve this plugin.

## Inspirations and Alternatives

The code here is highly inspired by the following plugins:

1. [swaldman/c3p0-play](https://github.com/swaldman/c3p0-play)
2. [autma/play-hikaricp-plugin](https://github.com/autma/play-hikaricp-plugin)

We decide to do our own because both plugins above looks unmaintained.

There are also two other alternatives using c3p0:

1. [hadashi/play2-c3p0-plugin](https://github.com/hadashi/play2-c3p0-plugin)
2. [Furyu/play-c3p0-plugin](https://github.com/Furyu/play-c3p0-plugin)

## References

1. [HikariCP Configuration](https://github.com/brettwooldridge/HikariCP): See information about what can be configured, supported data sources and drivers
2. [Play Plugins Docs](https://playframework.com/documentation/2.3.x/ScalaPlugins): Understand how plugins are created and configured
3. [Typesafe Config](https://github.com/typesafehub/config): What is supported and help with the syntax
4. [jdbcdslog-exp](https://code.google.com/p/jdbcdslog-exp/): How to configure the logSql enabler
5. [Heroku Docs](https://www.playframework.com/documentation/2.3.x/ProductionHeroku): Docs about how to deploy Play applications to Heroku.

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
