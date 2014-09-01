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
"Ron Johnson, a highway patrol captain who has been put in charge of policing the Ferguson protests, told a 2am press conference on Wednesday that officers arrested 47 people and recovered three loaded handguns. No shots were fired from the crowd and police were not forced to deploy teargas, he said.  The FBI announced last week that it was carrying out an investigation into the shooting of Brown on 9 August, which will determine whether it amounted to a civil rights violation. It may also be widened to examine police practices in Ferguson, a suburb of St Louis. Holder said in that in addition to 40 FBI agents, who had interviewed “hundreds” of people, he had deployed “some of the civil rights division’s most experienced prosecutors” to lead the investigation.  “Good law enforcement requires forging bonds of trust between the police and the public. This trust is all-important, but it is also fragile. It requires that force be used in appropriate ways,” he wrote.  The St Louis investigation has been clouded by the pressure on McCulloch, who has deep ties to law enforcement agencies. His impartiality was called into question when he criticised the decision by Nixon last week to remove the responsibility for policing the protests against the killing of Brown from the St Louis County police force.  “Enforcement priorities and arrest patterns must not lead to disparate treatment under the law, even if such treatment is unintended. And police forces should reflect the diversity of the communities they serve.”"
```

## License

Copyright © 2014 Matthew Gumport

Distributed under the Eclipse Public License version 1.0.
