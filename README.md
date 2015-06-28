# summarizer

[![Dependency Status](https://www.versioneye.com/user/projects/559057c231633800240003fb/badge.svg?style=flat)](https://www.versioneye.com/user/projects/559057c231633800240003fb)
[![Travis](https://img.shields.io/travis/gumpt/teaser-clj.svg)]()
[![Clojars](https://img.shields.io/clojars/v/teaser-clj.svg?style=flat)]()

A Clojure library that's a port of the [Python library](https://github.com/xiaoxu193/PyTeaser) that's a port of the [Scala library](https://github.com/MojoJolo/textteaser).

## Usage

### Leiningen

```
[teaser-clj "0.4.2"]
```

### Gradle

```
compile "teaser-clj:teaser-clj:0.4.2"
```

### Maven

```
<dependency>
  <groupId>teaser-clj</groupId>
  <artifactId>teaser-clj</artifactId>
  <version>0.4.2</version>
</dependency>
```

### In code

```clojure
user=> (require '[teaser-clj.core :refer [summarize-text]])
user=> (def sentences "(Reuters) - Twitter Inc said it has implemented a security technology that makes it harder to spy on its users and called on other Internet firms to do the same, as Web providers look to thwart spying by government intelligence agencies. The online messaging service, which began scrambling communications in 2011 using traditional HTTPS encryption, said on Friday it has added an advanced layer of protection for HTTPS known as 'forward secrecy.' 'A year and a half ago, Twitter was first served completely over HTTPS,' the company said in a blog posting. 'Since then, it has become clearer and clearer how important that step was to protecting our users' privacy.' Twitter's move is the latest response from US Internet firms following disclosures by former spy agency contractor Edward Snowden about widespread, classified US government surveillance programs. Facebook Inc, Google Inc, Microsoft Corp and Yahoo Inc have publicly complained that the government does not let them disclose data collection efforts. Some have adopted new privacy technologies to better secure user data. Forward secrecy prevents attackers from exploiting one potential weakness in HTTPS, which is that large quantities of data can be unscrambled if spies are able to steal a single private 'key' that is then used to encrypt all the data, said Dan Kaminsky, a well-known Internet security expert. The more advanced technique repeatedly creates individual keys as new communications sessions are opened, making it impossible to use a master key to decrypt them, Kaminsky said. 'It is a good thing to do,' he said. 'I'm glad this is the direction the industry is taking.'")
user=> (summarize-text "Twitter's 'Forward Secrecy' Takes Step To Make It Harder To Spy On Its Users" sentences)
"Facebook Inc, Google Inc, Microsoft Corp and Yahoo Inc have publicly complained that the government does not let them disclose data collection efforts.  The more advanced technique repeatedly creates individual keys as new communications sessions are opened, making it impossible to use a master key to decrypt them, Kaminsky said.  Some have adopted new privacy technologies to better secure user data.  'I'm glad this is the direction the industry is taking.'  'It is a good thing to do,' he said."
```

## For More

For a more thorough introduction to each part of the library, see [intro.md.](doc/intro.md)

## License

Copyright © 2014 Matthew Gumport

Distributed under the Eclipse Public License version 1.0.
