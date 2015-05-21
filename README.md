# HikariCP Plugin for 2.3.x

> This plugin is not necessary for applications using using **Play 2.4.0** because HikariCP is the default pool for Play 2.4.0. Anyway, we will try to keep this plugin updated with HikariCP releases to support developers using Play 2.3.x.

This plugin works with `2.3.x` of PlayFramework and uses version `2.3.7` of HikariCP.

Note, it can be made to work with Play `2.2.x` of the PlayFramework, but it requires changing the dependencies as the current build relies on the Play `2.3.x` plugin. Please, see the versions table below to see which version of Play is supported.

[![Build Status](https://travis-ci.org/edulify/play-hikaricp.edulify.com.svg?branch=master)](https://travis-ci.org/edulify/play-hikaricp.edulify.com) [![Issue Stats](http://issuestats.com/github/edulify/play-hikaricp.edulify.com/badge/pr)](http://issuestats.com/github/edulify/play-hikaricp.edulify.com) [![Dependency Status](https://www.versioneye.com/user/projects/54eaa687d1ec577c97000cd5/badge.svg?style=flat)](https://www.versioneye.com/user/projects/54eaa687d1ec577c97000cd5) [![Coverage Status](https://coveralls.io/repos/edulify/play-hikaricp.edulify.com/badge.svg?branch=master)](https://coveralls.io/r/edulify/play-hikaricp.edulify.com?branch=master) [![Codacy Badge](https://www.codacy.com/project/badge/9817a5d0538b434eb052ce5917a7ae9d)](https://www.codacy.com/public/contact_4/play-hikaricp.edulify.com)

## About this documentation

When reading this documentation at Github, select the tag that matches the version of the plugin that you are using.

## Why HikariCP?

[HikariCP](https://github.com/brettwooldridge/HikariCP) is supposed to be the fastest connection pool in Java land. But we did not start to use it because of speed, but because of its reliability. After suffering with connection leaks from BoneCP, we decide to implement our own database plugin to replace the default one. You can see [a discussion about database exceptions](https://groups.google.com/forum/#!topic/play-framework/7PBnjiXkNuU) caused by BoneCP (or misconfiguration of it). Also there are numerous other discussions about people having problems related to BoneCP.

Here is how HikariCP is working for us:

![HikariCP in Production](http://i.imgur.com/xPqkc7T.png)

## Versions

| Version | HikariCP | Play  | Readme note                     |
|--------:|---------:|------:|:---------------------------------|
| 2.0.5   | 2.3.7    | 2.3.8 | [Release 2.0.5](https://github.com/edulify/play-hikaricp.edulify.com/blob/2.0.5/README.md)|
| 2.0.4   | 2.3.6    | 2.3.8 | [Release 2.0.4](https://github.com/edulify/play-hikaricp.edulify.com/blob/2.0.4/README.md)|
| 2.0.3   | 2.3.5    | 2.3.8 | [Release 2.0.3](https://github.com/edulify/play-hikaricp.edulify.com/blob/2.0.3/README.md)|
| 2.0.2   | 2.3.2    | 2.3.8 | [Release 2.0.2](https://github.com/edulify/play-hikaricp.edulify.com/blob/2.0.2/README.md)|
| 2.0.1   | 2.3.2    | 2.3.8 | [Release 2.0.1](https://github.com/edulify/play-hikaricp.edulify.com/blob/2.0.1/README.md)|
| 2.0.0   | 2.3.2    | 2.3.8 | [Release 2.0.0](https://github.com/edulify/play-hikaricp.edulify.com/blob/2.0.0/README.md)|
| 1.5.2   | 2.3.2    | 2.3.8 | [Release 1.5.2](https://github.com/edulify/play-hikaricp.edulify.com/blob/1.5.2/README.md)|
| 1.5.1   | 2.2.5    | 2.3.7 | [Release 1.5.1](https://github.com/edulify/play-hikaricp.edulify.com/blob/1.5.1/README.md)|
| 1.5.0   | 2.0.1    | 2.3.4 | [Release 1.5.0](https://github.com/edulify/play-hikaricp.edulify.com/blob/1.5.0/README.md)|
| 1.4.1   | 2.0.1    | 2.3.2 | [Release 1.4.1](https://github.com/edulify/play-hikaricp.edulify.com/blob/1.4.1/README.md)|
| 1.4.0   | 1.4.0    | 2.3.1 | [Release 1.4.0](https://github.com/edulify/play-hikaricp.edulify.com/blob/1.4.0/README.md)|
| 1.3.1   | 1.3.8    | 2.3.1 | [Release 1.3.1](https://github.com/edulify/play-hikaricp.edulify.com/blob/1.3.1/README.md)|
| 1.3.0   | 1.3.8    | 2.3.1 | [Release 1.3.0](https://github.com/edulify/play-hikaricp.edulify.com/blob/1.3.0/README.md)|
| 1.2.0   | 1.3.8    | 2.2.3 | [Release 1.2.0](https://github.com/edulify/play-hikaricp.edulify.com/blob/1.2.0/README.md)|
| 1.1.0   | 1.3.8    | 2.2.3 | [Release 1.1.0](https://github.com/edulify/play-hikaricp.edulify.com/blob/1.1.0/README.md)|
| 1.0.0   | 1.3.5    | 2.2.2 | [Release 1.0.0](https://github.com/edulify/play-hikaricp.edulify.com/blob/1.1.0/README.md)|


For more information about what changed in each version, see the our CHANGELOG.md file.

## Repository

You need to add the following repository in order to use this module:

```scala
resolvers += Resolver.url("Edulify Repository", url("http://edulify.github.io/modules/releases/"))(Resolver.ivyStylePatterns)
```

## How to Use

There are just a few steps to properly configure the plugin:

### Step 1: Add the dependency

Add the following dependency to your `project/build.sbt` or `project/Build.scala`:

    "com.edulify" %% "play-hikaricp" % "2.0.5"

### Step 2: Disable default `dbplugin`

Add the following line to your `conf/application.conf`:

    dbplugin=disabled

This will disable `dbplugin` and avoids that BoneCP creates useless connections (which in some cases can create database problems, like exhaust available connections).

### Step 3: Enable HikariCP Plugin

Add the following line to your `conf/play.plugins`:

    200:com.edulify.play.hikaricp.HikariCPPlugin

Due to the fact that the [Play JPA plugin](https://github.com/playframework/playframework/blob/master/framework/src/play-java-jpa/src/main/resources/play.plugins) is assigned a priority of **400**, please make sure that you assign `com.edulify.play.hikaricp.HikariCPPlugin` a priority less than that when using datasources looked up via JNDI. Otherwise, during application startup when JPA attempts to create the `EntityManagerFactory` your data source will not have been bound to JNDI yet. [Play documentation](http://playframework.com/documentation/2.3.x/ScalaPlugins) states that connection pools should use a **200** priority.

### Step 4: Configure HikariCP

Please, before reading this section, we recommend that you get familiarized with [HikariCP configurations](https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby). It is the main source to understand each possible configuration for HikariCP and it have invaluable information about how to best configure your pool. We also recommends that you read about [Typesafe Config](https://github.com/typesafehub/config), which is the configuration language used by PlayFramework.

This plugin just read each possible HikariCP configuration and create the pool from it, with all the defaults honored. Here is a simple example of how to configure your pool in `application.conf`:

    db {
      default {
        driverClassName=org.postgresql.Driver
        jdbcUrl="jdbc:postgresql://localhost/simpsons"
        username=bart
        password=51mp50n
      }
    }

Alternatively, you can configure it using some implementation of `javax.sql.DataSource` provided by your JDBC Driver:

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

HikariCP does not offer (out of the box) a way to log SQL statements and it suggests that you use the log capacities of your database vendor. From HikariCP docs:

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

Keep in mind that **this is intended to be used just in development environments** and you should not configure it in production, since there is a performance degradation and it will polute your logs.

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

## Using with Slick

[play-slick](https://github.com/playframework/play-slick) requires a `db.<database>.driver` property to be configured. So, besides your pool configuration, when using Slick, you also have to configure this property. Per instance:

    db {
      default {
        driver=org.postgresql.Driver
        driverClassName=${db.default.driver}
        jdbcUrl="jdbc:postgresql://localhost/simpsons"
        username=bart
        password=51mp50n
      }
    }

Notice that the driver was repeated in two different properties, even if one is reference the other. After that, play-slick is smart enough to use the configured pool.

## Deploying to Heroku

When using Heroku, a `DATABASE_URL` environment variable in the form `scheme://user:password@host:port/db` will be created for you. This variable can be used directly inside `application.conf`:

    db {
      default {
        driverClassName=org.postgresql.Driver
        databaseUrl=${DATABASE_URL}
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
