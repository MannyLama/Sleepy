# Sleepy
#### *Get through the night*

## Info + configuration

Sleep is a plugin designed for SMP servers where it allows for the night to be skipped without not neccessarily everyone sleeping.
There are three modes which can be chosen when configuring the server to decide how many need to be sleeping:
* ALL           - Would require everyone in the world to sleep.
* HALF          - Would require half the people in the world to sleep (rounded down).
* [FIXED-VALUE] - Would require said value to be reached in the world before the night will reset (could be overriden by Vanillia).

Above all, there is a lot of flexibility and customisation in this plugin. You can customize every message which would show to the player. (Excl. version update + /sleepy) so that it can fit in nicely with whatever style your server may have. 

There is also an option to exclude worlds where the plugin would not function in, such as you may not want it to apply in the Factions world, but instead in the Survival world. (If you had a multi-gamemode, one server kinda setup).

## Update checker

The plugin utilizes a personal API server on a public endpoint to check the version of the plugin. This is used by all of my plugins. You can find the host at [My Website](https://elliepotato.de). You can read more about this API [Here](https://github.com/literallyEllie/elliepotato-api). You can disable this feature in the config.

**As of 28.07.2019, the API server is not live so it will not work, if you fork and build yourself, you can disable it in the configuration**

## Issues / Pull Requests
If you find a bug with the plugin, you can optionally make a PR and fix it yourself. Or alternatively make an issue report at https://github.com/literallyEllie/Sleepy/issues . I appreciate it eitherway. 
If you make a PR, please try and follow the style and conventions of the plugin (not to mention Java conventions) so that it fits in nicely.

## Building the project yourself.
This project requires **Gradle 5** & **Java 8** to build (because of ShadowJar mainly). Just clone the repository and import it was a Gradle project and the [build.gradle](https://github.com/literallyEllie/Sleepy/blob/master/build.gradle) should do the rest.

## Links + Contact
* [Plugin Spigot Page](spigotmc.org/resources/sleepy.69678/)
* [This GitHub Page](https://github.com/literallyEllie/Sleepy)
* [My website](https://elliepotato.de)
* My Discord: **Ellie#0006**
* My Twitter: **@literallyEllie@**


Thanks for reading and have a good day.
Ellie

