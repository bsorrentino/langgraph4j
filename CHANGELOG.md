# Changelog



<!-- "name: v1.0-beta2" is a release tag -->

## [v1.0-beta2](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-beta2) (2024-08-10)



### Documentation

 -  update readme ([6fe3cfab3b90028](https://github.com/bsorrentino/langgraph4j/commit/6fe3cfab3b90028de59435599035600a2ef79a02))

 -  update readme ([784b8af1883114f](https://github.com/bsorrentino/langgraph4j/commit/784b8af1883114f1c0c7946e69c223f06591b501))

 -  update changelog ([b505197e4c4b39e](https://github.com/bsorrentino/langgraph4j/commit/b505197e4c4b39ee21b3fcb3cd5e10cd5186534f))


### Refactor

 -  rename AgentExecutor.builder to AgentExecutor.graphBuilder ([7f2416657a7cff8](https://github.com/bsorrentino/langgraph4j/commit/7f2416657a7cff8272775b80320b798f030d034a))
   

### ALM 

 -  move to next version ([ed9fa6c3fe69e81](https://github.com/bsorrentino/langgraph4j/commit/ed9fa6c3fe69e810ace34d431055030ec43e7554))
   





<!-- "name: v1.0-20240809" is a release tag -->

## [v1.0-20240809](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20240809) (2024-08-09)

### Features

 *  add utitlity for support serialization of nullable value ([7a820294f37b9e1](https://github.com/bsorrentino/langgraph4j/commit/7a820294f37b9e1d361f2210295806fde5c59293))
   

### Bug Fixes

 -  update builder visibility to public ([bffa8a46fc7c228](https://github.com/bsorrentino/langgraph4j/commit/bffa8a46fc7c22884da02291947d9d8705b781a7))


### Documentation

 -  update changelog ([ad9d96752715baa](https://github.com/bsorrentino/langgraph4j/commit/ad9d96752715baac7b894b8c0816288f2a4f6124))








<!-- "name: v1.0-20240807-1" is a release tag -->

## [v1.0-20240807-1](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20240807-1) (2024-08-07)

### Features

 *  refine collection utilities ([1be0f7279663fb4](https://github.com/bsorrentino/langgraph4j/commit/1be0f7279663fb46b9d07eaf3546c7d31829db5c))
   
 *  enable fluent interface on graph definition ([787d41c3537821f](https://github.com/bsorrentino/langgraph4j/commit/787d41c3537821f731151359ada376b72832eba3))
     > deprecate: setEntryPoint, setFinishPoint, setConditionalEntryPoint
   
 *  add Channel support ([cd500132ef8b133](https://github.com/bsorrentino/langgraph4j/commit/cd500132ef8b13369823d16c75b7a775ffc176a8))
     > add reducer, default value provider
     > add AppenderChannel to manage accumulated list of values
     > deprecate AppendableValue
     > work on #13
   


### Documentation

 -  update readme ([475ffaba3ce93f1](https://github.com/bsorrentino/langgraph4j/commit/475ffaba3ce93f1e6f3b72bfc5e158028ace1287))


### Refactor

 -  use graph fluent interface ([b6ee47b842bde7c](https://github.com/bsorrentino/langgraph4j/commit/b6ee47b842bde7c7da33d5c7b178c686f14ddc6b))
   


### Continuous Integration

 -  rename deploy snapshot action ([7d1b273178c505d](https://github.com/bsorrentino/langgraph4j/commit/7d1b273178c505d975aa4343cf4bafc5e0d9eeae))
   
 -  rename deploy snapshot action ([06a2820cd43dfcc](https://github.com/bsorrentino/langgraph4j/commit/06a2820cd43dfccdbae4a8cd7695af98d9b4db17))
   
 -  rename deploy snapshot action ([59e44c91aae44bb](https://github.com/bsorrentino/langgraph4j/commit/59e44c91aae44bb950ee7da07643667e8e7be16f))
   
 -  refinement of deploy snapshot action ([4063d2b1c1654a1](https://github.com/bsorrentino/langgraph4j/commit/4063d2b1c1654a11333bd19e691d345606dc6838))
   




<!-- "name: v1.0-20240807" is a release tag -->

## [v1.0-20240807](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20240807) (2024-08-07)

### Features

 *  finalize Checkpoint implementation ([77e4723753a79b1](https://github.com/bsorrentino/langgraph4j/commit/77e4723753a79b1cb8d6a0e7b05c9da97216e93b))
     > resolve  #11
   
 *  finalize checkpoint implementation ([1564efca643c31c](https://github.com/bsorrentino/langgraph4j/commit/1564efca643c31c48df649d7729b646b3648f40f))
     > add AgentState and Checkpointer serializer
     > add support for MemorySaver
     > work on #11
   
 *  start implementing checkpoint ([f9800ec98253f50](https://github.com/bsorrentino/langgraph4j/commit/f9800ec98253f5015775ff088b5486ce4ea3c4d7))
     > - add BaseCheckpointSaver
     > - add Checkpoint
     > - add CheckpointConfig
     > - add CompileConfig
     > - add InvokeConfig
     > work on #11
   


### Documentation

 -  update readme ([9ed434ad02926ba](https://github.com/bsorrentino/langgraph4j/commit/9ed434ad02926ba253008f745fd648ec724e7e64))

 -  add description, scm, license ([3d2b2a9a260a6d4](https://github.com/bsorrentino/langgraph4j/commit/3d2b2a9a260a6d455621d48f65982fc56e5589b1))
     > work on #4


### Refactor

 -  make AppendableValueRW serializable ([a49decf1386fedd](https://github.com/bsorrentino/langgraph4j/commit/a49decf1386fedd1bf28ec417f19ee53847ee939))
    > work on #11


### ALM 

 -  move to next developer release ([5db6022ca330927](https://github.com/bsorrentino/langgraph4j/commit/5db6022ca33092780e0072075d144a70d73316e0))
   
 -  upgrade async-generator dependency, add slf4j to test scope ([b8ab321093899c6](https://github.com/bsorrentino/langgraph4j/commit/b8ab321093899c6e7293430d79bfc5ab94012736))
    > work on #11


### Continuous Integration

 -  add deploy snapshot action ([93074fbd0e0beef](https://github.com/bsorrentino/langgraph4j/commit/93074fbd0e0beef4d84284d290910ebc2e499415))
   
 -  add release profile ([47cb279dbba1a00](https://github.com/bsorrentino/langgraph4j/commit/47cb279dbba1a00d03ec3639ddfc3a0f8968d25d))
    > work on #4

 -  update deploy.yaml ([e5ef005e09f7b76](https://github.com/bsorrentino/langgraph4j/commit/e5ef005e09f7b762a9b766323005eda8442d93a3))
    > work on #4

 -  update deploy script ([1862707da26c5a9](https://github.com/bsorrentino/langgraph4j/commit/1862707da26c5a99506059146298ad4956a74739))
    > add sonatype token
 > work on #4

 -  update deploy script ([0585f8caeb80027](https://github.com/bsorrentino/langgraph4j/commit/0585f8caeb800272ff9a6735863a57aabf6b6418))
    > remove release profile
 > work on #4





<!-- "name: v1.0-beta1" is a release tag -->

## [v1.0-beta1](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-beta1) (2024-08-02)



### Documentation

 -  update readme ([c96574d32dd2395](https://github.com/bsorrentino/langgraph4j/commit/c96574d32dd2395ad4afe861a4b013e1f96b9573))

 -  update readme ([feeb46eb2b3f9e9](https://github.com/bsorrentino/langgraph4j/commit/feeb46eb2b3f9e9750f5a182c6d30b64c0a142f4))

 -  update changelog template ([c66fc6b1774cc90](https://github.com/bsorrentino/langgraph4j/commit/c66fc6b1774cc90e617668ae57bca1a0de02a80d))

 -  update readme ([13afc2265e2d4a2](https://github.com/bsorrentino/langgraph4j/commit/13afc2265e2d4a2409c1bde1bfc4f7f9f6f9899c))

 -  update changelog ([df07e2d4137abcb](https://github.com/bsorrentino/langgraph4j/commit/df07e2d4137abcb99656d579a1a669a1d845f6cc))


### Refactor

 -  rename core module from langgraph4j-jdk8 to langgraph4j--core-jdk8 ([ccf6282e9ab9d5e](https://github.com/bsorrentino/langgraph4j/commit/ccf6282e9ab9d5eba48adc8b56d307b9f4103b5e))
   

### ALM 

 -  update version to next release ([6c4d365ded24b5a](https://github.com/bsorrentino/langgraph4j/commit/6c4d365ded24b5ad83aace0bdfab848a0fe2887e))
   

### Continuous Integration

 -  add maven plugin for deployment ([3a195394e5b3379](https://github.com/bsorrentino/langgraph4j/commit/3a195394e5b33792b7340590ef6bd6195c1fb6ce))
    > working on #4

 -  add github action for deployment ([ab8db1d51e28c7e](https://github.com/bsorrentino/langgraph4j/commit/ab8db1d51e28c7ef99cb7867406fc4a6dccc4be1))
   




<!-- "name: v1.0-20240729" is a release tag -->

## [v1.0-20240729](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20240729) (2024-07-29)


### Bug Fixes

 -  **lg4j-graph**  svg height settings ([f4ae09f6fea0025](https://github.com/bsorrentino/langgraph4j/commit/f4ae09f6fea0025b8f9f3d21bb549f71ca08a1a2))

 -  remive resize handler on disconnection ([5b76da01f475aeb](https://github.com/bsorrentino/langgraph4j/commit/5b76da01f475aeb84a515d9637adfa4bfec6454f))


### Documentation

 -  update readme. refine changelog ([6e1a6864ef9b29a](https://github.com/bsorrentino/langgraph4j/commit/6e1a6864ef9b29a62b314d8a846af2c1b3c122f3))

 -  update changelog ([ab5fbc2666f13b3](https://github.com/bsorrentino/langgraph4j/commit/ab5fbc2666f13b37a0b068dcafd625414510bf5c))


### Refactor

 -  **web-app**  fix new distribution ([b1a377ebc65b7df](https://github.com/bsorrentino/langgraph4j/commit/b1a377ebc65b7df552ea5a292aa151719c014832))
   
 -  upgrade to langchain4j 0.33.0 ([afaf3274b20b523](https://github.com/bsorrentino/langgraph4j/commit/afaf3274b20b5235fa504cee8d3f9de95c570abc))
   
 -  **server-jetty**  load logging.properties from fs not from classpath anymore ([cd4f30737d3197a](https://github.com/bsorrentino/langgraph4j/commit/cd4f30737d3197a3d7f2eeb8366a61ebf48f5203))
   

### ALM 

 -  update distribution ([7082a1fbb7692db](https://github.com/bsorrentino/langgraph4j/commit/7082a1fbb7692db8b3095d46c5410dbe60312034))
   





<!-- "name: v1.0-20240723" is a release tag -->

## [v1.0-20240723](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20240723) (2024-07-23)

### Features

 *  **frontend**  add zoom support on graph view ([c6d7fab152c1edb](https://github.com/bsorrentino/langgraph4j/commit/c6d7fab152c1edbe8c39f098c612d4c0c267f0b3))
     > - switch to vanilla webcomponent t betetr control mermaid rendering
     > - add d3 zoom support
     > - remember zoom trasformation between rendering
   
 *  experiment using d3 zoom on svg ([44be1a1f52f6d20](https://github.com/bsorrentino/langgraph4j/commit/44be1a1f52f6d20df4fd47fbedfdc2e72aa9910f))
   
 *  **server-jetty**  set dark theme by default ([c4a06ec88e12332](https://github.com/bsorrentino/langgraph4j/commit/c4a06ec88e12332a08dc3cf2bd1c8f78ed8f0dd1))
   
 *  **server-jetty**  add adaptiveRAG test ([050c628f45e369a](https://github.com/bsorrentino/langgraph4j/commit/050c628f45e369a7290cb0561370474ab3b5729c))
   

### Bug Fixes

 -  **core**  generation graph ([df75b6db12a659f](https://github.com/bsorrentino/langgraph4j/commit/df75b6db12a659f4d7a93bb618bd877675bdd20a))
     > check printConditionalEdge on declareConditionalStart()


### Documentation

 -  update changelog ([dd7be4e71dd91a1](https://github.com/bsorrentino/langgraph4j/commit/dd7be4e71dd91a1e94c95a3522b3cb55e82f2305))

 -  update changelog ([a21c7a72a5f39f5](https://github.com/bsorrentino/langgraph4j/commit/a21c7a72a5f39f5cce8f36602457c50152e9f737))


### Refactor

 -  **frontend**  clean code ([d792b96b1c4a733](https://github.com/bsorrentino/langgraph4j/commit/d792b96b1c4a733c616fd95591f450e31756a4b3))
   
 -  **frontend**  : clean code ([36ec62756424f38](https://github.com/bsorrentino/langgraph4j/commit/36ec62756424f386e2439cbf97f33132377f4b54))
   
 -  **fornt-end**  lg4j-graph fills the parent size ([796b09d5f61349e](https://github.com/bsorrentino/langgraph4j/commit/796b09d5f61349e1776187674644fc7c449eb10e))
   

### ALM 

 -  **server**  update dist ([b640ce23393f190](https://github.com/bsorrentino/langgraph4j/commit/b640ce23393f19058a6fd08aa0b648b13aa86c03))
   
 -  **server-jetty**  update dist ([6577e93f7cd9520](https://github.com/bsorrentino/langgraph4j/commit/6577e93f7cd9520c181a92af02336f00624e9a9b))
   





<!-- "name: v1.0-20240719" is a release tag -->

## [v1.0-20240719](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20240719) (2024-07-19)

### Features

 *  toggle conditional-edge representation ([4e55eda05e23bf3](https://github.com/bsorrentino/langgraph4j/commit/4e55eda05e23bf3053f907833afde4c7f09a7e04))
   
 *  **front-end**  make result panel scrollable ([fbd73f12d10b77a](https://github.com/bsorrentino/langgraph4j/commit/fbd73f12d10b77a8dc6070619b25605bb21126ea))
   
 *  **server-jetty**  add completion of async context ([d67ef3f6d98de23](https://github.com/bsorrentino/langgraph4j/commit/d67ef3f6d98de2378efa0316997c9e6490bbf3cb))
   
 *  add @alenaksu/json-viewer component ([2cc3a69c2448965](https://github.com/bsorrentino/langgraph4j/commit/2cc3a69c2448965e01567f8f93aed4277b4ba7ea))
     > work on #9
   
 *  add support for custom mapper ([b0fe566790be739](https://github.com/bsorrentino/langgraph4j/commit/b0fe566790be7391afe5086d0ccb61e02b54fa06))
     > work on #9
   
 *  add agent executor sample ([d7ddb58e61e34d3](https://github.com/bsorrentino/langgraph4j/commit/d7ddb58e61e34d339bd488e842ac6adbf10ce469))
     > work on #9
   
 *  add support for custom title ([48ec649edf97477](https://github.com/bsorrentino/langgraph4j/commit/48ec649edf97477def6475fc989ab1b280070f97))
     > work on #9
   
 *  stream returns also 'start' and 'stop' steps ([bb6e0de5ccb8ca8](https://github.com/bsorrentino/langgraph4j/commit/bb6e0de5ccb8ca8e8305c102de9ff605be96bf12))
     > work on #9
   
 *  generate mermaid with node id ([7967a93439a6590](https://github.com/bsorrentino/langgraph4j/commit/7967a93439a6590546cb86c2883e839d1f804ee3))
     > need for node hightlight
     > work on #9
   
 *  finalize node highlight ([cd934894f2b8c8a](https://github.com/bsorrentino/langgraph4j/commit/cd934894f2b8c8a219db9ba34fd70d4426384c4d))
     > work on #9
   
 *  highlight active node ([feae491063ac3a5](https://github.com/bsorrentino/langgraph4j/commit/feae491063ac3a5f738328dc78e223b50cd78230))
     > work on #9
   
 *  move from war to jar packaging ([e942aefdbf96dc1](https://github.com/bsorrentino/langgraph4j/commit/e942aefdbf96dc155acc465ae384c1ff7f291e98))
     > better for embedding
     > work on #9
   
 *  back-end refinements ([bdec3a3e9828fe7](https://github.com/bsorrentino/langgraph4j/commit/bdec3a3e9828fe75b7a8ee9b50c6db8a52a9c492))
     > - log support
     > - return nodeoutput json representation
     > - update front-end distribution
     > work on #9
   
 *  front-end refinements ([f48618cdee8f095](https://github.com/bsorrentino/langgraph4j/commit/f48618cdee8f095c77714070b2eb67983395aab9))
     > - UI/UX refinements
     > - build input form from metadata
     > - improve result visualization
     > work on #9
   
 *  **server**  add builder ([9e8109d84887a3a](https://github.com/bsorrentino/langgraph4j/commit/9e8109d84887a3af535ba6dc9abb650e939b246b))
     > with support of:
     > - port
     > - inputArg metadata
     > work on #9
   
 *  **js**  finalize front-end candidate release ([33becfcec58795d](https://github.com/bsorrentino/langgraph4j/commit/33becfcec58795d750abe5b40ca62253f653510c))
     > work on #9
   
 *  **LangGraphStreamingServer**  implementation refinement ([3b8c6cf83100e2f](https://github.com/bsorrentino/langgraph4j/commit/3b8c6cf83100e2ff890f9b74f3461576f513e05a))
     > work on #9
   
 *  **jetty**  upgrade frontend dist ([3cf8b643e76e094](https://github.com/bsorrentino/langgraph4j/commit/3cf8b643e76e0945e4c6f2031be5af58720390be))
     > work on #9
   
 *  **core**  move on development version of async-iterator ([4d385b9bf9b739d](https://github.com/bsorrentino/langgraph4j/commit/4d385b9bf9b739dbb793a0eed4d28b63216043c5))
     > work on #9
   
 *  **LangGraphStreamingServer**  complete pilot implementation ([5ebfa769c20ed35](https://github.com/bsorrentino/langgraph4j/commit/5ebfa769c20ed35d4d09718906e03056afe08148))
     > work on #9
   
 *  update front-end dist ([0b3fc281afeb3b1](https://github.com/bsorrentino/langgraph4j/commit/0b3fc281afeb3b13e12c1f675af0abfd579edf58))
     > work on #9
   
 *  webapp frontend refinements ([920bae03c20315b](https://github.com/bsorrentino/langgraph4j/commit/920bae03c20315bea807b6bef271ed48d2f1ee42))
     > work on #9
   
 *  setup lit + tailwind project ([4ddc639a5dac0e0](https://github.com/bsorrentino/langgraph4j/commit/4ddc639a5dac0e0e26b10a43bc64b8aec74b21a7))
     > work on #9
   
 *  add http streaming support ([d57e9170056480c](https://github.com/bsorrentino/langgraph4j/commit/d57e9170056480ca71267957325cf469b09fbdc3))
   

### Bug Fixes

 -  mermaid loading diagram error ([5ccef4548baa66f](https://github.com/bsorrentino/langgraph4j/commit/5ccef4548baa66fd04d4acfce04e52d5801dea74))
     > avoid use of (deprecated) mermaidAPI


### Documentation

 -  update readme ([c45b04983271663](https://github.com/bsorrentino/langgraph4j/commit/c45b04983271663b688419ed569b263d4340c8b7))

 -  update readme ([30820697591c000](https://github.com/bsorrentino/langgraph4j/commit/30820697591c0001e6d7758faf103d9b009bd1f7))

 -  update readme ([bec7e46926765e2](https://github.com/bsorrentino/langgraph4j/commit/bec7e46926765e24779ec594efd48c5339ef1232))

 -  update changelog ([43966c93dfcd6c5](https://github.com/bsorrentino/langgraph4j/commit/43966c93dfcd6c5013307af13fbab91e8bcc3762))


### Refactor

 -  playground refinements ([1fd90b006dba0e4](https://github.com/bsorrentino/langgraph4j/commit/1fd90b006dba0e42ddcc216cd34fcf256c081c0b))
   
 -  rename server module ([88e6a7037fc6f52](https://github.com/bsorrentino/langgraph4j/commit/88e6a7037fc6f523ae6967164240dc80d4c765ea))
    > from &#x27;jetty&#x27; to &#x27;server-jetty&#x27;
 > resolve #9

 -  clean code ([eab97854fc8cd79](https://github.com/bsorrentino/langgraph4j/commit/eab97854fc8cd791c43e157fbb6adce3b5be0b1f))
    > work on #9

 -  clean code ([0e9bc7660f1522e](https://github.com/bsorrentino/langgraph4j/commit/0e9bc7660f1522e1ec3fa026cdb12947a882e0ad))
   
 -  add compile method ([7b795ff466e283d](https://github.com/bsorrentino/langgraph4j/commit/7b795ff466e283d8fc5930b758f638f95ad6ebdd))
    > - useful for streaming server impl
 > work on #9

 -  remove unused import ([9a339ce8bf52554](https://github.com/bsorrentino/langgraph4j/commit/9a339ce8bf52554bff7d4abeb06876732cf7b45d))
   
 -  update comment ([af609956e1b71c9](https://github.com/bsorrentino/langgraph4j/commit/af609956e1b71c9ee9b4513ae6af05ebc8065b99))
   

### ALM 

 -  **frontend**  update dist ([e96162e6752380c](https://github.com/bsorrentino/langgraph4j/commit/e96162e6752380c74b2ad157328c176c611d9ccc))
   
 -  **server**  update dist ([53dfb22ce7a73c1](https://github.com/bsorrentino/langgraph4j/commit/53dfb22ce7a73c10c07e629319a1693ce5a97103))
   
 -  update git ignore ([b79170a1028cc9d](https://github.com/bsorrentino/langgraph4j/commit/b79170a1028cc9dd297b75bd5a4839ead18d4553))
   
 -  update front-end dist ([22e943d435bf4c4](https://github.com/bsorrentino/langgraph4j/commit/22e943d435bf4c47321e84298d4fd9f3fec1bede))
    > work on #9

 -  update front-end dist ([153fc4f42e9bc80](https://github.com/bsorrentino/langgraph4j/commit/153fc4f42e9bc80ae934bb7684218ef9ba456787))
    > work on #9

 -  update fornt-end dist ([17e57501073be75](https://github.com/bsorrentino/langgraph4j/commit/17e57501073be75f9ec9d9f381b61a50200ab27b))
    > work on #9

 -  upgrade front-end dist ([38298373112466a](https://github.com/bsorrentino/langgraph4j/commit/38298373112466a28ba3d45f5cc545eae2205591))
    > work on #9

 -  upgrade java-async-generator lib ([4ba26ddddcf869f](https://github.com/bsorrentino/langgraph4j/commit/4ba26ddddcf869f3a370039f5662ffaf2c7d5435))
    > work on #9

 -  support of java8 and java17 building ([dc8ff48b8c1a233](https://github.com/bsorrentino/langgraph4j/commit/dc8ff48b8c1a2330ebf75dd81810be28df10bd9b))
    > work on #9






<!-- "name: v1.0-20240621" is a release tag -->

## [v1.0-20240621](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20240621) (2024-06-21)

### Features

 *  **core**  add support of Mermaid diagram-as-node syntax generation ([a0fd5a95a4d0493](https://github.com/bsorrentino/langgraph4j/commit/a0fd5a95a4d049355877cc8ac2a46a53d0a9d345))
     > resolve #5
   
 *  **core**  add support for contidional entrypoint in getGraph() method ([1a81fe399211a62](https://github.com/bsorrentino/langgraph4j/commit/1a81fe399211a62b8ccafedee67f7f56e330ba99))
   


### Documentation

 -  update readme ([ef7953b94c87190](https://github.com/bsorrentino/langgraph4j/commit/ef7953b94c8719023be2a29e214010c6e1bf7e93))

 -  **adaptive rag**  add mermaid diagram ([46d6fc535bf8de1](https://github.com/bsorrentino/langgraph4j/commit/46d6fc535bf8de1188ba862ad20acd9977ed0292))
     > resolve #5

 -  **adaptive-rag**  update readme ([47ec3b494836544](https://github.com/bsorrentino/langgraph4j/commit/47ec3b4948365440e3e84b54029a5e8e29a9a022))

 -  **adaptive-rag**  update readme ([5e11dd489628466](https://github.com/bsorrentino/langgraph4j/commit/5e11dd489628466fe997f2d60308a0c33d4c88f5))

 -  **adaptive-rag**  update readme ([c28381fb18938df](https://github.com/bsorrentino/langgraph4j/commit/c28381fb18938df71508aff88414375a8848e454))

 -  update changelog ([86996b108be06a7](https://github.com/bsorrentino/langgraph4j/commit/86996b108be06a7f543975bb348608de2c16282d))


### Refactor

 -  update project layout ([4cbd5c042052c32](https://github.com/bsorrentino/langgraph4j/commit/4cbd5c042052c32155600bcc7862745006b2a8a7))
   
 -  **adaptive-rag**  make opening Chroma Store lazy ([6892438d158e6dd](https://github.com/bsorrentino/langgraph4j/commit/6892438d158e6dd262a91271951534d15ca685ef))
    > resolve #5

 -  **core**  support of multiple diagram-as-node syntax generation ([9af787d3b85d03f](https://github.com/bsorrentino/langgraph4j/commit/9af787d3b85d03f6c2523e5b0d74736214403d1f))
    > make diagram as code generation through an abstract class
 > work on #5

 -  **adaptive-rag**  clean code ([53911383e137db7](https://github.com/bsorrentino/langgraph4j/commit/53911383e137db7fe74f718ed0ed0deb038acb70))
   

### ALM 

 -  **adaptive-rag**  add exec tasks ([985275c10292bfe](https://github.com/bsorrentino/langgraph4j/commit/985275c10292bfe7cd67fe632df58530aecc9f1c))
    > - upsert: Populate Chroma Vector store
 > - chroma: start Chroma Server
 > - app: start demo app






<!-- "name: v1.0-20240619" is a release tag -->

## [v1.0-20240619](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20240619) (2024-06-19)

### Features

 *  update example ([dd746afb3534e99](https://github.com/bsorrentino/langgraph4j/commit/dd746afb3534e9987ddc013eddebf6ce2d2a812d))
     > work on #6
   
 *  add maven exec plugin to run example ([19b55d7dc37e9bd](https://github.com/bsorrentino/langgraph4j/commit/19b55d7dc37e9bd54235b67fe609555b7a6cc249))
     > work on #6
   
 *  complete AdaptiveRag implementation ([e3d62406268951f](https://github.com/bsorrentino/langgraph4j/commit/e3d62406268951f0c2617246e7fe54f501615d8f))
     > resolve #6
   
 *  complete nodes and edges ([7ab1205eb8a66f2](https://github.com/bsorrentino/langgraph4j/commit/7ab1205eb8a66f23e8fc1b94e2ee6f680ebfc299))
     > work on #6
   
 *  add webSearch method ([383476f5a230fd8](https://github.com/bsorrentino/langgraph4j/commit/383476f5a230fd8131f418570acf8a33dcfc069e))
     > work on #6
   
 *  start adaptive rag implementation ([16a0aefe2155ce0](https://github.com/bsorrentino/langgraph4j/commit/16a0aefe2155ce0863f71939f64b70585ef2bcd2))
     > work on #6
   
 *  add tavily dependency ([7af44a68b18bac7](https://github.com/bsorrentino/langgraph4j/commit/7af44a68b18bac7351161145964c66daed019b53))
     > work on #6
   
 *  add question rewriter function object ([ba4664a974fa9a5](https://github.com/bsorrentino/langgraph4j/commit/ba4664a974fa9a5b6a7af30c4763ad449919437c))
     > work on #6
   
 *  add retrieval grader function object ([36674feafc7ceb7](https://github.com/bsorrentino/langgraph4j/commit/36674feafc7ceb7e18de01f02f2da2615f400fe1))
     > work on #6
   
 *  add tavily integration ([a79e5e5434ae45b](https://github.com/bsorrentino/langgraph4j/commit/a79e5e5434ae45be53943643635773400359c68e))
     > work on #6
   
 *  **adaptiverag**  start implementin adaptive rag ([538c5d72644ee6f](https://github.com/bsorrentino/langgraph4j/commit/538c5d72644ee6f9a0429c7a60839a11cac370a7))
     > 1. create docker compose to host chroma
     > 2. create docker container to upsert sample data
     > 3. start implementation + unit test
     > work on #6
   

### Bug Fixes

 -  remove api key ref ([6753a9a63c5bad4](https://github.com/bsorrentino/langgraph4j/commit/6753a9a63c5bad42124c6e20a1d182c4a10f0f9a))


### Documentation

 -  update readme ([370a18d8d8e8121](https://github.com/bsorrentino/langgraph4j/commit/370a18d8d8e812148c304daf01e08d172e6be1c8))

 -  update readme ([21df2aa7a0555fc](https://github.com/bsorrentino/langgraph4j/commit/21df2aa7a0555fc97d14f5abe3d62e406df4b1b8))
     > work on #6

 -  update readme ([e34f8155c1c4919](https://github.com/bsorrentino/langgraph4j/commit/e34f8155c1c49197e8e48e9589bf8f1913c276f3))

 -  update readme ([4152b9dbec18429](https://github.com/bsorrentino/langgraph4j/commit/4152b9dbec18429ea8f7cc709eb1364d6db6ec04))

 -  **GraphRepresentation**  update javadoc ([7cd31cc54d367bb](https://github.com/bsorrentino/langgraph4j/commit/7cd31cc54d367bb7176b87b321c58a5515083d28))

 -  update readme ([7f2d137325df1a7](https://github.com/bsorrentino/langgraph4j/commit/7f2d137325df1a73fa3bc23e714dc896ec0026bf))

 -  add changelog ([b9491d73dcd64a7](https://github.com/bsorrentino/langgraph4j/commit/b9491d73dcd64a7d53824c2855757eac5de36c2e))


### Refactor

 -  remove deprecated object ([7ca950ac0bb95a5](https://github.com/bsorrentino/langgraph4j/commit/7ca950ac0bb95a5f40c58fc664a86eb3339bfb4e))
    > work on #6

 -  remove useless images ([854699636ee7b3a](https://github.com/bsorrentino/langgraph4j/commit/854699636ee7b3abfa68709fe9fb3bf312dcb32f))
    > work on #6


### ALM 

 -  add .env sample ([8f755d14e6ae8ef](https://github.com/bsorrentino/langgraph4j/commit/8f755d14e6ae8efe064b04be779f99d03fbf5f9b))
   
 -  remove useless files ([a9d5e4dbc360ba9](https://github.com/bsorrentino/langgraph4j/commit/a9d5e4dbc360ba979eda0c4b47b3178924881552))
   
 -  add adaptive-rag module ([3552f238e7f262a](https://github.com/bsorrentino/langgraph4j/commit/3552f238e7f262a96ff54466e059d7bb1dce6d6d))
   
 -  upgrade langchain4j version ([9a78ff59429e326](https://github.com/bsorrentino/langgraph4j/commit/9a78ff59429e326ff3205b33563c490d6180c36e))
   





<!-- "name: v1.0-20240610" is a release tag -->

## [v1.0-20240610](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20240610) (2024-06-10)



### Documentation

 -  update javadoc ([62ef3598db2908f](https://github.com/bsorrentino/langgraph4j/commit/62ef3598db2908f6339d5b78cc73769ef8d1e5bd))

 -  update javadoc ([4fedaff7af27e4b](https://github.com/bsorrentino/langgraph4j/commit/4fedaff7af27e4b878c71afd5db9ae9124a732a1))

 -  **core-jdk8**  update project site ([20f03b989a343e2](https://github.com/bsorrentino/langgraph4j/commit/20f03b989a343e2375398798a7a450199f7d1c1b))

 -  update readme ([5e284f4d0263023](https://github.com/bsorrentino/langgraph4j/commit/5e284f4d02630232585854fc465ef6227f574d47))

 -  update readme ([3b435f05aa27df3](https://github.com/bsorrentino/langgraph4j/commit/3b435f05aa27df3c08fb8d7b41fdfef71d7e9682))

 -  update readme ([46a20691660be2f](https://github.com/bsorrentino/langgraph4j/commit/46a20691660be2f747276e1b69d5bbe9de67a44a))


### Refactor

 -  diagram code generation method ([6be2a8e7e2ced6a](https://github.com/bsorrentino/langgraph4j/commit/6be2a8e7e2ced6af76fc61d85dbf92ab83899490))
   


### Continuous Integration

 -  **deploy-pages**  set jdk8 ([fe67a69e0300784](https://github.com/bsorrentino/langgraph4j/commit/fe67a69e0300784b4403d683130d74d93d877ec0))
   
 -  setup deploy-pages action ([0c5c855e9c51761](https://github.com/bsorrentino/langgraph4j/commit/0c5c855e9c51761cb65282ff419ddd00323b46a9))
   




<!-- "name: v1.0-20240520" is a release tag -->

## [v1.0-20240520](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20240520) (2024-05-20)

### Features

 *  refine PlantUML graph generation ([bd61ecb5cc4bfe7](https://github.com/bsorrentino/langgraph4j/commit/bd61ecb5cc4bfe717b270fbb55f9c8c644914879))
   
 *  generation of plantuml from graph definition ([7e8a739ce8581ec](https://github.com/bsorrentino/langgraph4j/commit/7e8a739ce8581ec40a1c7005425f1abe78a9b454))
   


### Documentation

 -  update readme ([847ace83f146e57](https://github.com/bsorrentino/langgraph4j/commit/847ace83f146e5756c8f5d0427aa0373efcaf5ac))

 -  update readme ([35daca70c65eae3](https://github.com/bsorrentino/langgraph4j/commit/35daca70c65eae3082effcb35d2e191c27c8b76c))

 -  update documantation ([e70f2ccc2682d84](https://github.com/bsorrentino/langgraph4j/commit/e70f2ccc2682d84bdff5b35ab7e808158bcfd4a5))


### Refactor

 -  clean code ([daac0e8e71eb56f](https://github.com/bsorrentino/langgraph4j/commit/daac0e8e71eb56f6c059ef25b547fa9cab50ab32))
   
 -  GraphState to StateGraph ([cfa7c92d65483ea](https://github.com/bsorrentino/langgraph4j/commit/cfa7c92d65483ea9a0970bd1ae7e7c3286c1e8e6))
    > make compliant to original LangGraph







<!-- "name: v1.0-20240516" is a release tag -->

## [v1.0-20240516](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20240516) (2024-05-16)

### Features

 *  **iamge_to_diagram**  add sub-graph for error review ([149705364f0265b](https://github.com/bsorrentino/langgraph4j/commit/149705364f0265bd94f935a2b0f829214a3c878f))
   


### Documentation

 -  update readme ([876c68284f38521](https://github.com/bsorrentino/langgraph4j/commit/876c68284f38521b74b60e51b5e1f83fe4040191))








<!-- "name: v1.0-20240514" is a release tag -->

## [v1.0-20240514](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20240514) (2024-05-14)

### Features

 *  **agent-jdk8**  finalize image_to_diagram_with_correction graph implementation ([bc1ef69d21c7f98](https://github.com/bsorrentino/langgraph4j/commit/bc1ef69d21c7f98060fc077da861f2a1a62576b8))
   
 *  **agent-jdk8**  implementing ImageToDiagram auto correction process ([e6e89e63bd2156a](https://github.com/bsorrentino/langgraph4j/commit/e6e89e63bd2156abfb44452e7f49748c7940dec2))
     > 1. evaluate generated diagram
     > 2. catch errors
     > 3. submit errors to Agent Review
     > 4. Generate new diagram
     > 5. got to (1)
   
 *  add image to diagram use case ([0d7d09f1ba1e49b](https://github.com/bsorrentino/langgraph4j/commit/0d7d09f1ba1e49ba72afce75bdf47e5b4f5fc60f))
   


### Documentation

 -  update readme ([e8803d6278b450c](https://github.com/bsorrentino/langgraph4j/commit/e8803d6278b450c0fdb72b62eaaad2aea4b05da5))


### Refactor

 -  remove jdk17 modules ([3fe06d652cb065e](https://github.com/bsorrentino/langgraph4j/commit/3fe06d652cb065e14d18dccb18d17cb4dfbe2cfb))
   
 -  **core-jdk8**  Agent State Management ([7e19f1e8fc6e731](https://github.com/bsorrentino/langgraph4j/commit/7e19f1e8fc6e731a8def851e81100f05e752dbcf))
    > - AgentState from interface to concrete class
 > - AppendableValue a readonly interface
 > - Create internal AppendableValueRW to update state

 -  rename method ([4c196bff8442030](https://github.com/bsorrentino/langgraph4j/commit/4c196bff8442030c2ed65996bebcd3ce155e7b74))
    > addConditionalEdge to addConditionalEdges

 -  change packages layout ([f42c01b32eabcf4](https://github.com/bsorrentino/langgraph4j/commit/f42c01b32eabcf45cef67f8338b82ea65713139f))
   
 -  rewrite async utils ([a8851730971ebcd](https://github.com/bsorrentino/langgraph4j/commit/a8851730971ebcd5b0ef47c9c44b458541b0426d))
    > jdk8
 > jdk17 and above


### ALM 

 -  update artifactId ([5995f6e148cd8a6](https://github.com/bsorrentino/langgraph4j/commit/5995f6e148cd8a6d8bb14e7682a1fffd3a9f4d17))
   
 -  add utility scripts ([0ca2e51e3e746de](https://github.com/bsorrentino/langgraph4j/commit/0ca2e51e3e746de8b33cf34c37e78f6e6e298b1e))
   
 -  add .gitignore ([4cace7dbf3a4a93](https://github.com/bsorrentino/langgraph4j/commit/4cace7dbf3a4a93b70978f4eb113e2975ab53e6f))
   
 -  add sync-generator-jdk8 deps ([007a8fbbf24ac60](https://github.com/bsorrentino/langgraph4j/commit/007a8fbbf24ac60d5d5478477d7333be62adf625))
   
 -  upgrade langchain4j deps ([d80fdc6d9d38d86](https://github.com/bsorrentino/langgraph4j/commit/d80fdc6d9d38d867a2555de85d306bf570b068a3))
   





<!-- "name: v1.0-20240329" is a release tag -->

## [v1.0-20240329](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20240329) (2024-03-29)

### Features

 *  create modules for supporting jdk8 and jdk17 and above ([fa604bc1fbff4d8](https://github.com/bsorrentino/langgraph4j/commit/fa604bc1fbff4d8c5322ec8db6495a4acf22e09f))
   
 *  Enable agent to process more function calls ([9acbca23c35454d](https://github.com/bsorrentino/langgraph4j/commit/9acbca23c35454d1112fcdb524bb89268e334083))
   
 *  finalize developing langchain4j agentexecutor using langgraph4j ([7dd851cc9a63284](https://github.com/bsorrentino/langgraph4j/commit/7dd851cc9a632848609898c97561ec38a5900449))
   
 *  add AsyncIterator support ([ddac14de830e781](https://github.com/bsorrentino/langgraph4j/commit/ddac14de830e78164a146bdeb56912e47a4c9a33))
     > experimental feature
   
 *  implement workflow's run ([9a5b2e230aa652f](https://github.com/bsorrentino/langgraph4j/commit/9a5b2e230aa652f62fea669885894845b38c4e8b))
   
 *  initial implementation ([dc46c9b49847c52](https://github.com/bsorrentino/langgraph4j/commit/dc46c9b49847c52b6ff48d4414c0b23cfcf70352))
     > graph creation
     > graph compilation
   

### Bug Fixes

 -  check initial state value ([2d67f97b76f3a53](https://github.com/bsorrentino/langgraph4j/commit/2d67f97b76f3a53fd3fe9b0db409c08818340be1))

 -  Agent extend conversation with assistant's reply ([d9bf1a3e698a7e7](https://github.com/bsorrentino/langgraph4j/commit/d9bf1a3e698a7e71ace5d0b48046efe80f51727a))


### Documentation

 -  update readme ([bf5ba9fba8ff94d](https://github.com/bsorrentino/langgraph4j/commit/bf5ba9fba8ff94dc036faf3c60728df6422c51ae))


### Refactor

 -  finalize modules ([2a94541e46c4765](https://github.com/bsorrentino/langgraph4j/commit/2a94541e46c4765d953697106b50fb23db311c53))
    > jdk8
 > jdk17 and above

 -  finalize modules ([28380891947d7ba](https://github.com/bsorrentino/langgraph4j/commit/28380891947d7ba812ecd632faaae5773f0e9659))
    > jdk8
 > jdk17 and above

 -  remove var usage ([9d6b6eb7dabb7a6](https://github.com/bsorrentino/langgraph4j/commit/9d6b6eb7dabb7a6b0e11bb24ebdaf1a5a9db2620))
   
 -  **agents**  skip deployment ([2fcbf2224758ce0](https://github.com/bsorrentino/langgraph4j/commit/2fcbf2224758ce0ef0b97869f6614872de2f0835))
   
 -  update groupid ([11d601efd996d58](https://github.com/bsorrentino/langgraph4j/commit/11d601efd996d5892b7900ba46d970c46dcc2cdf))
   
 -  move DotEnvConfig in test ([4cfbd68a1edce63](https://github.com/bsorrentino/langgraph4j/commit/4cfbd68a1edce63f09a3dd7810f6ed91e4ad436d))
   
 -  move DotEnvConfig in test ([7038ecb4d8d833d](https://github.com/bsorrentino/langgraph4j/commit/7038ecb4d8d833d62783a71c33418923a679c30b))
   
 -  made AppendableValue immutable ([0ead59d7445d0b6](https://github.com/bsorrentino/langgraph4j/commit/0ead59d7445d0b66db1444e5788127d6f2c4c55b))
   
 -  use string block for prompt template ([ef5df2e9ae9d202](https://github.com/bsorrentino/langgraph4j/commit/ef5df2e9ae9d202bf7abb211d8041f9aabf80e6e))
   
 -  start developing langchain4j agentexecutor using langgraph4j ([cb3cf804a2a6257](https://github.com/bsorrentino/langgraph4j/commit/cb3cf804a2a625706bc1eb44fcb0f4f7a10f6e7f))
    > add maven multi-module layout
 > add module for demo
 > starting implements Agent class

 -  finalize AsyncIterator support ([f404e50f06e6832](https://github.com/bsorrentino/langgraph4j/commit/f404e50f06e6832afe25e024f6a8a61a8b270501))
    > experimental feature

 -  refine AsyncIterator support ([19b43fdb42bf64c](https://github.com/bsorrentino/langgraph4j/commit/19b43fdb42bf64ca0bfdbb32cf09d94d411eea07))
    > experimental feature

 -  refine AsyncIterator support ([e29517be766c0e3](https://github.com/bsorrentino/langgraph4j/commit/e29517be766c0e33cd30cab1f028af472d16eb71))
    > experimental feature

 -  create SyncSubmissionPublisher ([261b537494c1c5f](https://github.com/bsorrentino/langgraph4j/commit/261b537494c1c5f690fbac96c2fbe91b6a8000b6))
    > publishing already happen in a thread, seems not useful use an async submission

 -  update package tree ([f03f90780523832](https://github.com/bsorrentino/langgraph4j/commit/f03f90780523832a8ab20b9d89ab7efa9a4d9ee3))
   

### ALM 

 -  add distribution management info ([3a46d2676c56362](https://github.com/bsorrentino/langgraph4j/commit/3a46d2676c56362ec2aa9ea066b24fe56360626b))
   
 -  skip test on building ([ccaf2da369d3c4c](https://github.com/bsorrentino/langgraph4j/commit/ccaf2da369d3c4ca12bbceaac8bac5fa3ebeb69d))
   
 -  update git ignore ([7b6275466d06e04](https://github.com/bsorrentino/langgraph4j/commit/7b6275466d06e0437747aec25b16626cd50152a5))
   
 -  update ignore ([12218f697c9cfc1](https://github.com/bsorrentino/langgraph4j/commit/12218f697c9cfc1f0d60d3eece1e21611a3bd204))
   
 -  add git ignore ([b3c0a9ee7056bcd](https://github.com/bsorrentino/langgraph4j/commit/b3c0a9ee7056bcd35b05722bd7f9d397e3c9ff1e))
   



