# afternode-commons

<a href="https://central.sonatype.com/namespace/cn.afternode.commons"><img alt="maven-central" height="40" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/available/maven-central_vector.svg"></a>

[![](https://jitpack.io/v/AFterNode/afternode-commons.svg)](https://jitpack.io/#AFterNode/afternode-commons)

Utilities for Minecraft bukkit plugin (and more) developing

## Modules
- **bukkit**: Command/Events registration, configuration serializer/deserializer and message builders
  - **bukkit-kotlin** Kotlin edition of bukkit module
- **commons**: Reflections and misc
- **adventure-messaging** Utilities for [Adventure](https://docs.advntr.dev/)
- **bungee** Utilities for BungeeCord (and forks)

# How to use

## Stable releases
![Maven Central Version](https://img.shields.io/maven-central/v/cn.afternode.commons/commons)

**Maven**
```xml
<dependency>
  <groupId>cn.afternode.commons</groupId>
  <artifactId>MODULE</artifactId>
  <version>VERSION</version>
</dependency>
```

**Gradle**
```groovy
implementation "cn.afternode.commons:MODULE:VERSION"
```
