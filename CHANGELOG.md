# Changelog

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
