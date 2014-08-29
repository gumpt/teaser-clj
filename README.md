# summarizer

A Clojure library that's a port of the [Python library](https://github.com/xiaoxu193/PyTeaser) that's a port of the [Scala library](https://github.com/MojoJolo/textteaser).

## Usage

### Leiningen

```
[teaser-clj "0.1.0-SNAPSHOT"]
```

### Gradle

```
compile "teaser-clj:teaser-clj:0.1.0-SNAPSHOT"
```

### Maven

```
<dependency>
  <groupId>teaser-clj</groupId>
  <artifactId>teaser-clj</artifactId>
  <version>0.1.0-SNAPSHOT</version>
</dependency>
```

### In code

```clojure
user=> (require '[teaser-clj/teaser.core :refer :all])
user=> (summarize-url "http://www.theguardian.com/world/2014/aug/20/attorney-general-holder-swift-investigation-michael-brown-ferguson")
"ron johnson, a highway patrol captain who has been put in charge of policing the ferguson protests, told a 2am press conference on wednesday that officers arrested 47 people and recovered three loaded handguns. no shots were fired from the crowd and police were not forced to deploy teargas, he said.\n“good law enforcement requires forging bonds of trust between the police and the public. this trust is all-important, but it is also fragile. it requires that force be used in appropriate ways,” he wrote.\n“enforcement priorities and arrest patterns must not lead to disparate treatment under the law, even if such treatment is unintended. and police forces should reflect the diversity of the communities they serve.”\nthe fbi announced last week that it was carrying out an investigation into the shooting of brown on 9 august, which will determine whether it amounted to a civil rights violation. it may also be widened to examine police practices in ferguson, a suburb of st louis. holder said in that in addition to 40 fbi agents, who had interviewed “hundreds” of people, he had deployed “some of the civil rights division’s most experienced prosecutors” to lead the investigation.\nthe st louis investigation has been clouded by the pressure on mcculloch, who has deep ties to law enforcement agencies. his impartiality was called into question when he criticised the decision by nixon last week to remove the responsibility for policing the protests against the killing of brown from the st louis county police force."
```

## License

Copyright © 2014 Matthew Gumport

Distributed under the Eclipse Public License version 1.0.
