# Changelog

## Version 2.0.6 - 2015/05/31

1. Upgrade Playframework to version 2.3.9
2. Upgrade HikariCP to version 2.3.8

## Version 2.0.5 - 2015/05/05

1. PR-66 GH-65 - Config translation for basic jdbc connection settings
2. Updates HikariCP to version 2.3.7

## Version 2.0.4 - 2015/04/14

1. PR-60 - Add sensible pool names by default
2. Updates HikariCP to version 2.3.6

## Version 2.0.3 - 2015/03/26

1. GH-59 - Allow play to start when a configured db is unavailable
2. Upgrade HikariCP to version 2.3.5

## Version 2.0.2 - 2015/02/25

1. GH-52 - Correct error report when database connection fail

## Version 2.0.1 - 2015/02/25

1. PR-54 - Added databaseUrl to allow for direct usage of DATABASE_URL on Heroku

## Version 2.0.0 - 2015/02/24

This is a full rewrite of this plugin and it is not backwards compatible with previous versions. We did that in order to have:

1. Simplified configuration: now it just exposes the HikariCP properties which can be configured directly in `conf/application.conf` file
2. Better documentation: it is more clear, concise and direct
3. Automated tests: now we have a specs suite
4. Java 8 support

Also, a lot of issues were closed in this release:

1. [#3 - Play way is not correct](https://github.com/edulify/play-hikaricp.edulify.com/issues/3)
2. [#25 - Cannot find config](https://github.com/edulify/play-hikaricp.edulify.com/issues/25)
3. [#27 - Instructions to use latest hikari plugin with Play 2.2.x](https://github.com/edulify/play-hikaricp.edulify.com/issues/27)
4. [#35 - Problem loading the file hikaricp.prod.properties](https://github.com/edulify/play-hikaricp.edulify.com/issues/35)
5. [#36 - Dynamically translate typesafe HOCON properties to Hikari properties](https://github.com/edulify/play-hikaricp.edulify.com/issues/36)
6. [#40 - Get rid of "jdbcConnectionTest" warning when using Play configuration](https://github.com/edulify/play-hikaricp.edulify.com/issues/40)
7. [#41 - Bad mapping for some properties & incoherent default values](https://github.com/edulify/play-hikaricp.edulify.com/issues/41)
8. [#43 - Invalid time conversion on IdleMaxAge](https://github.com/edulify/play-hikaricp.edulify.com/issues/43)
9. [#50 - Info level messages leaking into logs despite error level set](https://github.com/edulify/play-hikaricp.edulify.com/issues/50)
10. [#51 - It seems that 2.0.0-M1 cannot be resolved from the repository](https://github.com/edulify/play-hikaricp.edulify.com/issues/51)

## Version 1.5.2 - 2015/02/17

1. PR-42 & PR-48 - Fix properties names (`autocommit`, `isolation` and `readOnly`) while translating Play configuration to HikariCP configuration
2. PR-44 - Fix `idleMaxAge` time conversion
3. PR-46 - Updates documentation about how to use in Heroku deployments
4. PR-47 - Explicit duration unit in config parser
5. PR-49 - Update min and max pool size with play and hikari defaults
6. Updates Playframework to version 2.3.8

## Version 1.5.1 - 2014/12/16

1. PR-34 - Update dependency to HikariCP 2.2.5 and Play 2.3.7

## Version 1.5.0 - 2014/09/08

1. PR-24 & PR-23 - Code cleanup and more robust configuration
2. PR-21 - Embrace fail-fast principle for configuration parsing
3. PR-19 & PR-18 - Better property mapping between Play and HikariCP
2. GH-4 - Remove all deprecated properties

## Version 1.4.1 - 2014/07/31

1. GH-15 & PR-16 - Updates HikariCP, Scala and Play
2. PR-14 - Allow disabling through plays application.conf
3. GH-5 & PR-12 - Mask any password related properties

## Version 1.4.0 - 2014/07/05

1. GH-7 & PR-10 - Now it is possible to specify a JNDI datasource
2. Updates HikariCP to version 1.4.0 (see [more details here](https://groups.google.com/forum/#!topic/hikari-cp/_JAOvTJZS94))

## Version 1.3.1 - 2014/06/30

1. GH-9 - Corrects artifact name from play-*hirakicp* to play-**hikaricp**

## Version 1.3.0 - 2014/06/26

1. PR-8 - Updates to Play 2.3.1 and Scala 2.11.1
2. Builds across other scala versions (2.9.2, 2.10.4)

## Version 1.2.0 - 2014/05/04

1. GH-2 - Supports configuration using environment variables

## Version 1.1.0 - 2014/05/02

1. Updates to HikariCP 1.3.8
2. Uses config `minimumIdle` instead of `minimumPoolSize`
3. Updates to Play 2.2.3

## Version 1.0.0 - 2014/04/06

1. First release, with HikariCP 1.3.5
