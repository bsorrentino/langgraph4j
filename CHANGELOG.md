# Changelog



<!-- "name: v1.0.0" is a release tag -->

## [v1.0.0](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0.0) (2024-11-15)



### Documentation

 -  replace new version refs ([a06e5408dece2ec](https://github.com/bsorrentino/langgraph4j/commit/a06e5408dece2ec18edccff0b734c972264bf685))

 -  finalize serializer doc ([24cf52b065306ff](https://github.com/bsorrentino/langgraph4j/commit/24cf52b065306ffbbe813b9c6cfc4090ca737fef))
     > resolve #38

 -  site refinement ([aecd65ef0df55e9](https://github.com/bsorrentino/langgraph4j/commit/aecd65ef0df55e94b14520d15746d7ed149f1447))

 -  update notebooks run ([4ef6c3ae9a45ce9](https://github.com/bsorrentino/langgraph4j/commit/4ef6c3ae9a45ce922f4cc36cf00c14ab10064e94))

 -  update readme ([a6e691aaffebf60](https://github.com/bsorrentino/langgraph4j/commit/a6e691aaffebf6021bd439feaac989dc984a7c8c))



### ALM 

 -  bump new langchain4j version ([0dc4ff72d2c039b](https://github.com/bsorrentino/langgraph4j/commit/0dc4ff72d2c039b4937e7e97c094328af94c20e2))
   
 -  bump to new official release ([7d2867f7d7e27fb](https://github.com/bsorrentino/langgraph4j/commit/7d2867f7d7e27fb932e030a3937ebbbf50b173a1))
   





<!-- "name: v1.0-20241113" is a release tag -->

## [v1.0-20241113](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20241113) (2024-11-13)

### Features

 *  **StreamingOutput**  add toString() ([c8916f44303b350](https://github.com/bsorrentino/langgraph4j/commit/c8916f44303b350ed8da0a62b4dc7c02130cdc44))
   


### Documentation

 -  **core**  update sime menu ([8573a6e830e3d40](https://github.com/bsorrentino/langgraph4j/commit/8573a6e830e3d40f36a2d770bf097251a7a21a44))



### ALM 

 -  bump next intermediate version ([a2da7cc63ab9751](https://github.com/bsorrentino/langgraph4j/commit/a2da7cc63ab975168c4afb1d5ea73703aa9519bd))
   
 -  bump to snapshot ([511fb622550973a](https://github.com/bsorrentino/langgraph4j/commit/511fb622550973a0a1889f903056a8b6559dfa5e))
   





<!-- "name: v1.0-20241112" is a release tag -->

## [v1.0-20241112](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20241112) (2024-11-12)

### Features

 *  **AgentExecutor**  allow to use dynamic tool definirion ([e463bfe87059da7](https://github.com/bsorrentino/langgraph4j/commit/e463bfe87059da7d1984b9e6582c04b23b5d3da1))
   

### Bug Fixes

 -  **PlantUMLgenerator**  use right finish node ([6a7a0b9ed539f83](https://github.com/bsorrentino/langgraph4j/commit/6a7a0b9ed539f83408c1491bbcd7ca067e3a2f3c))


### Documentation

 -  update readme ([04fac5f56576c1c](https://github.com/bsorrentino/langgraph4j/commit/04fac5f56576c1cb373d5fea02eb8fa5dada70ab))
     > resolve #47

 -  update readme ([a16cd11b4f9ea93](https://github.com/bsorrentino/langgraph4j/commit/a16cd11b4f9ea939208c61ed22c1d3b2234638d6))

 -  update agent-executor docs ([2ab48f37b8fc09b](https://github.com/bsorrentino/langgraph4j/commit/2ab48f37b8fc09bee96f6cb9896257855e1af75b))

 -  update javadoc ([3006717053c6bb1](https://github.com/bsorrentino/langgraph4j/commit/3006717053c6bb1dfac8b1732793c23d4f5d31cd))



### ALM 

 -  bump to next intermediate version ([bb5dcce671d766f](https://github.com/bsorrentino/langgraph4j/commit/bb5dcce671d766f0952ac67a4caa7b3201a0559b))
   
 -  bump SNAPSHOT ([f20897527b8f644](https://github.com/bsorrentino/langgraph4j/commit/f20897527b8f644a73d10e1851b0deabcc6f5a73))
   





<!-- "name: v1.0-20241111" is a release tag -->

## [v1.0-20241111](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20241111) (2024-11-11)

### Features

 *  **ToolNode**  Support for Tools specified dynamically ([d5d387baba0fa40](https://github.com/bsorrentino/langgraph4j/commit/d5d387baba0fa407ab117965c18f2df0de6fcef7))
     > resolve #44
   
 *  **AsyncNodeGenerator**  add 'resumedFromEmbed' state management in AsyncNodeGenerator ([d4a5dd2f65d1e3f](https://github.com/bsorrentino/langgraph4j/commit/d4a5dd2f65d1e3f5ee929b74b0e4c907429164a0))
     > work on #31
   
 *  **LLMStreamingGenerator**  make compliant with StreamingOutput ([d39eef962b417bd](https://github.com/bsorrentino/langgraph4j/commit/d39eef962b417bdd65c85d45857c9c7b248289cb))
     > work on #31
   
 *  **StreamingOutput**  add NodeOutput<> specialization to support LLM streaming ([aed9c23c09ed37d](https://github.com/bsorrentino/langgraph4j/commit/aed9c23c09ed37df535b25673776e4bdce1669e7))
     > work on #31
   
 *  **langchain4j**  bump async-generator version ([253cd7d5b1ed353](https://github.com/bsorrentino/langgraph4j/commit/253cd7d5b1ed353897a819b6220cbe4de854d038))
     > work on #31
   
 *  **langchain4j**  handle completion on LLMStreamingGenerator ([5740d1394aa35a6](https://github.com/bsorrentino/langgraph4j/commit/5740d1394aa35a6ddc83605078958924cfbb5ebb))
     > - provide an operator to convert response to Map&lt;&gt;
     > work on #31
   
 *  **CompiledGraph**  integrate embedding generator. ([e7938e49c137f12](https://github.com/bsorrentino/langgraph4j/commit/e7938e49c137f12671665213d19cd700947fe990))
     > - manage resume state after completion of an embed generator
     > work on #31
   
 *  **CompiledGraph**  add support of AsyncIterator.WithEmbed ([3c254e8e800a346](https://github.com/bsorrentino/langgraph4j/commit/3c254e8e800a346feeab63e1f5436ae1dc87c098))
     > work on #31
   
 *  make LLMStreamingGenerator an implementation of AsyncGenerator.WithResult ([c838f618ce9553c](https://github.com/bsorrentino/langgraph4j/commit/c838f618ce9553cc118f2919ab6c72e1dd298850))
     > work on #31
   
 *  add a LLMStreamingGenerator class ([d38eac5586b0ecd](https://github.com/bsorrentino/langgraph4j/commit/d38eac5586b0ecdfe1c190e099b07210c496faf6))
     > Convert the langchain4j StreamingResponseHandler to AsyncStream
     > work on #31
   
 *  add react demo notebook ([b19ab5e7456d4bc](https://github.com/bsorrentino/langgraph4j/commit/b19ab5e7456d4bc58fad6145c238400069cb06ea))
   


### Documentation

 -  refine serializer documentation ([74c9c33b7c5110c](https://github.com/bsorrentino/langgraph4j/commit/74c9c33b7c5110c984325838cbc6275435469bb8))
     > work on #38

 -  update changeme ([c33bddf902eac64](https://github.com/bsorrentino/langgraph4j/commit/c33bddf902eac64cd07851f925051a4723574a4e))



### ALM 

 -  bump intermediate version ([a43baab610d37ba](https://github.com/bsorrentino/langgraph4j/commit/a43baab610d37ba0e742a1102bd711f5da5caff0))
   
 -  bump to SNAPSHOT ([9d0a9a6be7b5825](https://github.com/bsorrentino/langgraph4j/commit/9d0a9a6be7b582588a06b594ebe9e88e5db558dc))
    > bump async-generator-jdk8 dep
 > work on #31

 -  bump to SNAPSHOT ([232eead50f7e2b9](https://github.com/bsorrentino/langgraph4j/commit/232eead50f7e2b9850af57112eb1e22c5cb2d94d))
   





<!-- "name: v1.0-20241029" is a release tag -->

## [v1.0-20241029](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20241029) (2024-10-29)

### Features

 *  **serializable**  remove ClassHolder,  StringHolder to avoid class loading problem ([a137abd7c5bea6f](https://github.com/bsorrentino/langgraph4j/commit/a137abd7c5bea6f90a1c19705d981270370160f6))
   
 *  add file system checkpoint saver ([5d8036d5ceabe99](https://github.com/bsorrentino/langgraph4j/commit/5d8036d5ceabe998458fbecb2449b9cbe01cae51))
     > work on #35
   
 *  **studio**  decouple the Studio implementation from Jetty Server ([98f070e6fa910b7](https://github.com/bsorrentino/langgraph4j/commit/98f070e6fa910b79c3e0c405a439d95d63f42fac))
     > resolve #42
   

### Bug Fixes

 -  **agentexecutor**  set jackson mapper visibility to ANY ([3cb700a3ed6da50](https://github.com/bsorrentino/langgraph4j/commit/3cb700a3ed6da50f64418b2b9068d1d13e5edce3))


### Documentation

 -  update changeme ([33d0b1e14367a4f](https://github.com/bsorrentino/langgraph4j/commit/33d0b1e14367a4ff9d2d6561181d815ed2bbab71))



### ALM 

 -  bump to new intermediate version ([be65332d27f2ad0](https://github.com/bsorrentino/langgraph4j/commit/be65332d27f2ad05d7e3422f28f35244c67b9507))
   
 -  bump to SNAPSHOT ([571d941c4537c66](https://github.com/bsorrentino/langgraph4j/commit/571d941c4537c661a554e76bd3f9baddddeaca19))
   
 -  **studio**  DTS refactoring ([ead9d9b3ca8f98e](https://github.com/bsorrentino/langgraph4j/commit/ead9d9b3ca8f98e52cbd51dc97cf40d0578f7b43))
    > work on #42

 -  **springai-agentexecutor**  skip site generation ([caa34b0b7b76bc7](https://github.com/bsorrentino/langgraph4j/commit/caa34b0b7b76bc78666d97c4d8ed3c79c6f04404))
   
 -  update actions ([39ac81de3847ea4](https://github.com/bsorrentino/langgraph4j/commit/39ac81de3847ea41af472d3de62a32a3a7b2d20d))
   





<!-- "name: v1.0-rc2" is a release tag -->

## [v1.0-rc2](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-rc2) (2024-10-25)


### Bug Fixes

 -  **pom**  add relative parent path ([3902dcb101d38c3](https://github.com/bsorrentino/langgraph4j/commit/3902dcb101d38c3496b76073f80964f8163e73d4))


### Documentation

 -  update docs with new version ([dcd765c0901b1e3](https://github.com/bsorrentino/langgraph4j/commit/dcd765c0901b1e36644ee5edd626d96b7e405c5c))

 -  update site pages ([5b8109c9d1af32d](https://github.com/bsorrentino/langgraph4j/commit/5b8109c9d1af32d54231da54d90e76042cd17d17))

 -  update README ([791b930e6de4ff6](https://github.com/bsorrentino/langgraph4j/commit/791b930e6de4ff68e60f94f2afe7cceb7e5b98f8))

 -  update changeme ([9f84f2d2f12eca4](https://github.com/bsorrentino/langgraph4j/commit/9f84f2d2f12eca41c83ed5bfb6c8fb95889e48c3))



### ALM 

 -  bump to new release candidate ([2fbb7e8c24f220f](https://github.com/bsorrentino/langgraph4j/commit/2fbb7e8c24f220f6ae961629ecc67e3920fc6e54))
   
 -  **action**  update deploy actions ([3d7153b2df8b46e](https://github.com/bsorrentino/langgraph4j/commit/3d7153b2df8b46e3c86a4d85d6f3d66716138af6))
    > select projects to deploy

 -  bump to SNAPSHOT version ([d7c167d5afca22f](https://github.com/bsorrentino/langgraph4j/commit/d7c167d5afca22fe587a689cc42113c775d96ff5))
   
 -  **image-to-diagram**  skip release ([8d54ba0778461f2](https://github.com/bsorrentino/langgraph4j/commit/8d54ba0778461f2e5f85bcad9ce3ef50f4d9ebd2))
   
 -  **image-to-diagram**  skip release ([5a9c47e4efa952b](https://github.com/bsorrentino/langgraph4j/commit/5a9c47e4efa952bf4720de1e6b443034e4384738))
   





<!-- "name: v1.0-20241024" is a release tag -->

## [v1.0-20241024](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20241024) (2024-10-24)

### Features

 *  add  sample using langgraph4j with springai ([069b1dae671419d](https://github.com/bsorrentino/langgraph4j/commit/069b1dae671419da124eea56bd1efd1f4e3a9954))
     > resolve #15
   
 *  **serializer**  change the standard serialization implementation to avoid class loading issues ([13fed3780bec648](https://github.com/bsorrentino/langgraph4j/commit/13fed3780bec648b8d419a315cd91f877bcbd0ce))
   


### Documentation

 -  remove BaseSerializer refs ([f0fc1a43417f03b](https://github.com/bsorrentino/langgraph4j/commit/f0fc1a43417f03b9c0f716c1ab88f6707ba149d6))

 -  update samples links ([90be73e67ed7189](https://github.com/bsorrentino/langgraph4j/commit/90be73e67ed7189a39d5fb6828b03c92acd799de))

 -  add serializer section ([a8d3f8e1e441fee](https://github.com/bsorrentino/langgraph4j/commit/a8d3f8e1e441fee2553f97dc0a952edc1f193d69))
     > work on #38

 -  update feature list ([ada695313e79123](https://github.com/bsorrentino/langgraph4j/commit/ada695313e791235ac3f9b44d47ce6a9172b8f4c))

 -  update project names ([e457098a54ccef4](https://github.com/bsorrentino/langgraph4j/commit/e457098a54ccef4a07a037816fc281d414d02239))

 -  update site docs ([7994f159cb9465e](https://github.com/bsorrentino/langgraph4j/commit/7994f159cb9465efc698ae609ea930221f69520f))

 -  update changeme ([8ab856f4743733f](https://github.com/bsorrentino/langgraph4j/commit/8ab856f4743733f0eff2027c1060be6526072150))


### Refactor

 -  update to project DTS ([9de853487ecc665](https://github.com/bsorrentino/langgraph4j/commit/9de853487ecc665eb0ad9469f120e1873dd7b64e))
    > added samples folder

 -  **agentexecutor**  update to be compliant with new serialization implementation ([6a408b2868e4fc7](https://github.com/bsorrentino/langgraph4j/commit/6a408b2868e4fc701eb643ec7cbbab8ad43b3c3c))
   
 -  **notebook**  update to be compliant with new serialization implementation ([cbba10a0f551a56](https://github.com/bsorrentino/langgraph4j/commit/cbba10a0f551a5652791d441f57cf44f9a46f58e))
   
 -  update to be compliant with new serialization implementation ([baf6fae5d9c6170](https://github.com/bsorrentino/langgraph4j/commit/baf6fae5d9c6170953fb7c5c95373af12de04f4a))
   

### ALM 

 -  bump to intermediate version ([c547d2035945345](https://github.com/bsorrentino/langgraph4j/commit/c547d2035945345b016b8bcec93873352810f190))
   
 -  move to next dev version ([9c2b09a81374734](https://github.com/bsorrentino/langgraph4j/commit/9c2b09a81374734fa9ba55f1e17c5404a97df80b))
   
 -  update deploy action ([e8e2c1dbd6cbf7e](https://github.com/bsorrentino/langgraph4j/commit/e8e2c1dbd6cbf7ee86c88e60153f3119b06d6fed))
    > remove JDK 8






<!-- "name: v1.0-rc1" is a release tag -->

## [v1.0-rc1](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-rc1) (2024-10-12)



### Documentation

 -  update readme ([b49b34cd912f406](https://github.com/bsorrentino/langgraph4j/commit/b49b34cd912f406dc74a379bdbf04e0cc9c3919d))

 -  update changeme ([07bb1763ba09206](https://github.com/bsorrentino/langgraph4j/commit/07bb1763ba09206ac35b57699b3e76044371f90c))



### ALM 

 -  bump to new version ([edf8bad65f537ec](https://github.com/bsorrentino/langgraph4j/commit/edf8bad65f537ecfd089e71a5bbaf8bfd94eab3a))
   





<!-- "name: v1.0-20241011" is a release tag -->

## [v1.0-20241011](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20241011) (2024-10-11)

### Features

 *  refine Serialization implementation ([199ae8d396d6e27](https://github.com/bsorrentino/langgraph4j/commit/199ae8d396d6e27e0087647b6eeeac5a78c441d1))
     > -  add StateSerializer abstract class that owns a StateFactory
     > -  refactor tests, samples and how-tos accordly
     > work on #29
   
 *  create branch on CompileGraph.updateState() ([0f1ecdb1cca8d5a](https://github.com/bsorrentino/langgraph4j/commit/0f1ecdb1cca8d5ab2a09b39316932c5e2e86f1e4))
   
 *  **serialization**  make serialization implementation supporting more serialization strategies ([23af74bf35514c9](https://github.com/bsorrentino/langgraph4j/commit/23af74bf35514c9f56fd9afdcc162f1d2c73fa33))
     > resolve #29
   

### Bug Fixes

 -  **serializer**  propagate ObjectOutputMapper and ObjectInputMapper ([1693b43314936db](https://github.com/bsorrentino/langgraph4j/commit/1693b43314936dbe8eacbf1ba599ad5407819376))


### Documentation

 -  overall site refinements ([4de05122e142e93](https://github.com/bsorrentino/langgraph4j/commit/4de05122e142e930e14ffd95d0a2df71014efd32))

 -  rename server-jetty to studio-jetty ([00f0d49b4872442](https://github.com/bsorrentino/langgraph4j/commit/00f0d49b48724420b6a72c171b98c756967df548))

 -  rename server-jetty to studio-jetty ([e0d5c7c646c5a8a](https://github.com/bsorrentino/langgraph4j/commit/e0d5c7c646c5a8a993f795cfb3abf22d9f15a0ac))

 -  rename server-jetty to studio-jetty ([d7cc59285a027c8](https://github.com/bsorrentino/langgraph4j/commit/d7cc59285a027c8a8764fb19846b7f072edf774b))

 -  update changeme ([fd5194ceb6ee8ff](https://github.com/bsorrentino/langgraph4j/commit/fd5194ceb6ee8ff9dd9181e7b986b5c660b579c7))


### Refactor

 -  Renamed LangGraphStreamingServer to LangGraphStreamingServerJetty ([022695c2dab9a7d](https://github.com/bsorrentino/langgraph4j/commit/022695c2dab9a7d0b7cc3a15b75be3b65e2815c2))
   
 -  update how-tos with new serializer ([72d0e33b24d83b9](https://github.com/bsorrentino/langgraph4j/commit/72d0e33b24d83b9f652ac898f60fd451ee153d29))
    > work on #29

 -  set public scope ([9cb9d84c59d0bc9](https://github.com/bsorrentino/langgraph4j/commit/9cb9d84c59d0bc97ff30aca2f45292e4b2cddf7e))
   
 -  **action**  deploy aggregate site to pages ([852e95c080ed056](https://github.com/bsorrentino/langgraph4j/commit/852e95c080ed056f5c9319e4c09a06ffb384a893))
    > work on #36

 -  remove usage of lombok.var ([bb2c73cb5b2c423](https://github.com/bsorrentino/langgraph4j/commit/bb2c73cb5b2c423ee33185c27b4358485e857102))
    > work on #36

 -  **deploy**  rename server-jetty to studio-jetty ([31594fca57ea79a](https://github.com/bsorrentino/langgraph4j/commit/31594fca57ea79a6b20fe558dcb7bd9be30bb31b))
   

### ALM 

 -  bump to new intermediate version ([68f92ed62519e79](https://github.com/bsorrentino/langgraph4j/commit/68f92ed62519e79602fba1d7acb2ce0ef3aaef1c))
   
 -  setup maven site aggegation ([c928339ddb546c8](https://github.com/bsorrentino/langgraph4j/commit/c928339ddb546c8e62c9b7c98c07f8ae678e5aca))
    > resolve #36

 -  bump to next developer version ([80ff8fdae63cbee](https://github.com/bsorrentino/langgraph4j/commit/80ff8fdae63cbee19ec997915ef9ec8ec5ed3ede))
   





<!-- "name: v1.0-20241006" is a release tag -->

## [v1.0-20241006](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20241006) (2024-10-06)

### Features

 *  **frondend**  add support for resume action ([1a4125dc08d1e41](https://github.com/bsorrentino/langgraph4j/commit/1a4125dc08d1e41c99c7467315dd439d101b341c))
     > solve #34
   
 *  **server**  add support for JSON deserialization ([817efeaeff620cc](https://github.com/bsorrentino/langgraph4j/commit/817efeaeff620cc07a05e3cc290384d69bba75e7))
     > work on #34
   
 *  **ToolNode**  add logging support ([5a982f426207881](https://github.com/bsorrentino/langgraph4j/commit/5a982f426207881ccf51148078a72f596ce1df6a))
   
 *  improve streammode management ([fcd5d6790208aca](https://github.com/bsorrentino/langgraph4j/commit/fcd5d6790208aca538b1a7da17c771a21fc87325))
     > work on #34
   
 *  **agentexecutor**  add JSON serialization support ([16d01791530f046](https://github.com/bsorrentino/langgraph4j/commit/16d01791530f0468e28ade34a1e8d28488fe62b6))
     > work on #34
   
 *  **agentexecutor**  add JSON serialization support ([3fa5ec0207f9315](https://github.com/bsorrentino/langgraph4j/commit/3fa5ec0207f9315277fbf2bcc4315c5f82bc0e29))
     > work on #34
   
 *  move agent-executor to jdk17 modules ([a7ca4d7aabb4c0c](https://github.com/bsorrentino/langgraph4j/commit/a7ca4d7aabb4c0c6a69c4cff152cde105cb5989b))
   
 *  **server**  add resume management ([bf030a74a9863f4](https://github.com/bsorrentino/langgraph4j/commit/bf030a74a9863f4526f09c4cf487f9a61a112077))
     > work on #34
   
 *  add agentexecutor notebook ([1465519e3309fb1](https://github.com/bsorrentino/langgraph4j/commit/1465519e3309fb1bf58d67ae76565d2ab31e1346))
   
 *  **frontend**  add support for state update ([4cfa9c1e9d3bfee](https://github.com/bsorrentino/langgraph4j/commit/4cfa9c1e9d3bfee5c207ee9f531114b33ca24ac4))
     > work on #34
   
 *  **frontend**  add support for checkpoint ([1c86f6852e6ab64](https://github.com/bsorrentino/langgraph4j/commit/1c86f6852e6ab645c52e7f8643a8852e10bb0cf2))
     > work #34
   
 *  **frontend**  parametrize 'collapsed' in NodeOutput ([78e453ee918b361](https://github.com/bsorrentino/langgraph4j/commit/78e453ee918b36180daa51200516137b9135d73d))
     > work on #34
   
 *  **frontend**  add support for state editing ([ccbe25383b0ef74](https://github.com/bsorrentino/langgraph4j/commit/ccbe25383b0ef74fa142b57b233bd562d8cc2e64))
     > add react component @microlink/react-json-view
     > work on #34
   
 *  allow customize Serializer using CompileConfig ([af0d3d65d51d047](https://github.com/bsorrentino/langgraph4j/commit/af0d3d65d51d0473a7c368be748968eb965955a8))
   

### Bug Fixes

 -  **frontend**  remove method duplication ([6768dcaca7b2a43](https://github.com/bsorrentino/langgraph4j/commit/6768dcaca7b2a438ea82193db6e3808b300ca7c9))
     > work on #24

 -  update scripts ([bae7e1a82bb9f32](https://github.com/bsorrentino/langgraph4j/commit/bae7e1a82bb9f32b505d203abeed874e9fc363b6))


### Documentation

 -  update readme ([d7dd7e77e2e6e83](https://github.com/bsorrentino/langgraph4j/commit/d7dd7e77e2e6e8358a1626cf24eac232dbcf4b31))

 -  add readme ([2456df4a7c24835](https://github.com/bsorrentino/langgraph4j/commit/2456df4a7c2483545f658f8373a42520f6932f58))

 -  add readme ([19dc1a5a3469b90](https://github.com/bsorrentino/langgraph4j/commit/19dc1a5a3469b90cd642b304fbdbb3c022280ef5))

 -  update comments ([b55692d98b1da2a](https://github.com/bsorrentino/langgraph4j/commit/b55692d98b1da2ab76501f1614bbef58bf5bac0d))


### Refactor

 -  rebrand from server to studio ([cd14b6e0f4d8052](https://github.com/bsorrentino/langgraph4j/commit/cd14b6e0f4d80527fb1823e5098b05ce3d554a0b))
   
 -  **agentexecutor**  rename package ([b158f62bc1ee716](https://github.com/bsorrentino/langgraph4j/commit/b158f62bc1ee71629ca3cb169a23e8357f0adaaa))
   
 -  rename MapSerialize to MapSerializer ([a60a78bcb600e46](https://github.com/bsorrentino/langgraph4j/commit/a60a78bcb600e465037926e2981f53176b26cb6a))
   
 -  **agentexecutor**  rename package ([5a2a50dec339fad](https://github.com/bsorrentino/langgraph4j/commit/5a2a50dec339fada133cce4fce5f45f320581705))
   
 -  **agentexecutor**  rename package ([0edb34814d505ab](https://github.com/bsorrentino/langgraph4j/commit/0edb34814d505aba273294112ec1f1c414859464))
   
 -  **server**  remove nodes from initialization ([1cf01acd23da255](https://github.com/bsorrentino/langgraph4j/commit/1cf01acd23da255fb922c1d4180ca214d9261e5e))
    > work on #34


### ALM 

 -  bump new intermediate version ([a2bf300573a929e](https://github.com/bsorrentino/langgraph4j/commit/a2bf300573a929edd7124cb932087dd8fc4c46f5))
   
 -  update frontend dist ([c6fc457ad3749fd](https://github.com/bsorrentino/langgraph4j/commit/c6fc457ad3749fd013cb8909120cd84bac151196))
    > work on #34

 -  update frontend dist ([ab51fbe2eae4243](https://github.com/bsorrentino/langgraph4j/commit/ab51fbe2eae424392da839a2c6c0cfb23e8b5577))
    > work on #34

 -  update frontend dist ([348c1421e9207b0](https://github.com/bsorrentino/langgraph4j/commit/348c1421e9207b01b5b5f5a6c7b796bedc81f820))
    > work on #34

 -  update frontend dist ([e02ea9bfb4a5b03](https://github.com/bsorrentino/langgraph4j/commit/e02ea9bfb4a5b03aef8577c08e3ebe5cb44a4b21))
    > work on #34

 -  update frontend dist ([ba1060a379d0a6a](https://github.com/bsorrentino/langgraph4j/commit/ba1060a379d0a6acbaaa54f6f28a60695423cbca))
    > work on #34

 -  update frontend dist ([d1a49576c8009ff](https://github.com/bsorrentino/langgraph4j/commit/d1a49576c8009ff99b8c176a5b29c7e2f1ab7f58))
    > work on #34

 -  bump to SNAPSHOT ([4b2e260aea75814](https://github.com/bsorrentino/langgraph4j/commit/4b2e260aea75814fb8442f7004d18e8740f07570))
   





<!-- "name: v1.0-20240926" is a release tag -->

## [v1.0-20240926](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20240926) (2024-09-26)

### Features

 *  improve logging ([ec148d763587456](https://github.com/bsorrentino/langgraph4j/commit/ec148d76358745683711b496f7e55df2f1724c83))
   
 *  add module with langchain4j integration utility ([769887576ec20f0](https://github.com/bsorrentino/langgraph4j/commit/769887576ec20f028907e730c8e71d0a6ddda9ce))
     > - Add Message Serializers
     > - Add ToolExecutionRequestSerializer
     > - Add ToolNode utility
     > resolve #26
   


### Documentation

 -  update readme ([19c3ad0253ff1d4](https://github.com/bsorrentino/langgraph4j/commit/19c3ad0253ff1d45e428dd7dac9169d4b9858cfa))


### Refactor

 -  **serializer**  move from static to instance methods ([4870289cc21587c](https://github.com/bsorrentino/langgraph4j/commit/4870289cc21587c493386c34d41ed2b9297c8b4a))
   
 -  **how-ts**  use of new langchain4j integration module ([ed2163bf7c62789](https://github.com/bsorrentino/langgraph4j/commit/ed2163bf7c6278960846ead490e13a0bed32c334))
    > work on #26

 -  **AgentExecutor**  use onf new langchain4j integration module ([369ef5d06277ba9](https://github.com/bsorrentino/langgraph4j/commit/369ef5d06277ba98ab2400d5ef7c9afd5182c565))
    > work on #26

 -  **adaptiverag**  update system prompt ([6ec6ab67f85ef14](https://github.com/bsorrentino/langgraph4j/commit/6ec6ab67f85ef14be4852325fba68567dc009f5c))
    > work on #32

 -  update adaptiverag notebook ([40f78ac94728ebf](https://github.com/bsorrentino/langgraph4j/commit/40f78ac94728ebfc8cfd66e0e65f879a1f515ea8))
   

### ALM 

 -  bump to intermediate version ([94a2de96aa7c3ca](https://github.com/bsorrentino/langgraph4j/commit/94a2de96aa7c3ca9624330e5a3b8ea8124153ddb))
   
 -  bump to new version of langchain4j 0.35.0 ([cee29a8b0a9e137](https://github.com/bsorrentino/langgraph4j/commit/cee29a8b0a9e137d3dc927ead21bfde1e378c448))
   
 -  bump to SNAPSHOT ([3225dd47184425f](https://github.com/bsorrentino/langgraph4j/commit/3225dd47184425f37fa38ce7b1542514a9179c58))
   





<!-- "name: v1.0-20240924" is a release tag -->

## [v1.0-20240924](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20240924) (2024-09-24)


### Bug Fixes

 -  **AnswerGrader**  update prompt pattern ([af72f94b8710f1c](https://github.com/bsorrentino/langgraph4j/commit/af72f94b8710f1c3e7359b94993a97c67c9b7337))
     > solve #32


### Documentation

 -  update readme ([cb806a45c87a0ed](https://github.com/bsorrentino/langgraph4j/commit/cb806a45c87a0edad3a7393a44fb0adfc88eaa86))

 -  update readme ([3cc326f6034cd29](https://github.com/bsorrentino/langgraph4j/commit/3cc326f6034cd29cef792bc92084ba383c5a6dda))

 -  update changeme ([8fcaabafda3ebf3](https://github.com/bsorrentino/langgraph4j/commit/8fcaabafda3ebf3d428bb1ed6c428b3d8f25d784))



### ALM 

 -  bump to SNAPSHOT version ([e009e33b2149ab6](https://github.com/bsorrentino/langgraph4j/commit/e009e33b2149ab6a747565aa4d9b45cca088ac69))
   

### Continuous Integration

 -  bump to develop version ([b547c0563bd9658](https://github.com/bsorrentino/langgraph4j/commit/b547c0563bd9658c1f43b1ba3fb9b5f665159194))
   




<!-- "name: v1.0-beta5" is a release tag -->

## [v1.0-beta5](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-beta5) (2024-09-24)

### Features

 *  **Server**  finalize thread support ([3f4ee84236c1aa6](https://github.com/bsorrentino/langgraph4j/commit/3f4ee84236c1aa6a3838e2730c62560774231507))
     > - NodeOutput Json Serialization
     > - read thread from get parameter
     > - add thread on straming result
     > resolve #24
   
 *  **backend**  add threads into init servlet ([1f0f20afe80db2c](https://github.com/bsorrentino/langgraph4j/commit/1f0f20afe80db2ce1338988bcfa1773d0cc470d9))
     > work on #24
   
 *  set initial thread ([525286a37f767a4](https://github.com/bsorrentino/langgraph4j/commit/525286a37f767a4134bf8d764a4e552ed3ac1bfd))
     > work on #24
   
 *  **frontend**  thread management refinement ([24042d4b65b505a](https://github.com/bsorrentino/langgraph4j/commit/24042d4b65b505ae5703e6e593400d27b9a5d9cd))
     > work on #24
   
 *  **CompiledGraph**  add streamSnapshots() method ([11fc73be5e7a89f](https://github.com/bsorrentino/langgraph4j/commit/11fc73be5e7a89f1b00212aa7414a6332e7fce98))
     > work on #24
   
 *  allow stream return subclass of NodeOutput ([780b2b90bf56af2](https://github.com/bsorrentino/langgraph4j/commit/780b2b90bf56af2156d518bbba715824bd6912d1))
     > allow return of StateSnapshot
     > work on #24
   
 *  **frontend**  add support for thread(tab) switch ([39b651d67ac0f7a](https://github.com/bsorrentino/langgraph4j/commit/39b651d67ac0f7a4242c04b53034f963ec3dcdd9))
     > work on #24
   
 *  **AsyncNodeGenerator**  add output factory method ([0f612363c68f9e6](https://github.com/bsorrentino/langgraph4j/commit/0f612363c68f9e61ea85ace04c83ed85bf138766))
     > work on #24
   
 *  **CompliedGraph**  update AsyncGenerator implementation ([04bcd136803086a](https://github.com/bsorrentino/langgraph4j/commit/04bcd136803086aad7b9a859be14c3f00fa3eac1))
     > remove AsyncGeneratorQueue
     > add a custom AsyncNodeGenerator
     > update unit test
   
 *  **RunnableConfig**  add StreamMode enum ([40fad25c6b2e327](https://github.com/bsorrentino/langgraph4j/commit/40fad25c6b2e32721ed91fdedd3d019e88fdbfe9))
     > work on #24
   
 *  add toString() ([7445a88be87845f](https://github.com/bsorrentino/langgraph4j/commit/7445a88be87845f6974bb9462cb2394da6e94189))
   
 *  **server**  threads Implementation refinements ([3e291dbc62b98c0](https://github.com/bsorrentino/langgraph4j/commit/3e291dbc62b98c081d3a82ea1c01f8cf722578cf))
     > start preparing backend and fronend to manage threads
     > work on #24
   
 *  move main implementation of getGraph() on StateGraph ([39da1f4c07db473](https://github.com/bsorrentino/langgraph4j/commit/39da1f4c07db47352331b7d2ada873196561a6fd))
     > work on #24
   
 *  move main implementation of getGraph() on StateGraph ([9c1b39b2f5fd5ab](https://github.com/bsorrentino/langgraph4j/commit/9c1b39b2f5fd5ab752ba4be0736f09d674dac87f))
     > work on #24
   
 *  **collection**  add last() and lastMinus() support for the List<T> ([52bfbec8084adfb](https://github.com/bsorrentino/langgraph4j/commit/52bfbec8084adfbadab505e5f08b28adf941a637))
     > work on #24
   
 *  **serializer**  add support for mimetype ([7ca1a6169376932](https://github.com/bsorrentino/langgraph4j/commit/7ca1a6169376932ab95abe557d09f95eb9190fa3))
     > work on #24
   
 *  **frontend**  add thread button ([75f975e9bea7ce8](https://github.com/bsorrentino/langgraph4j/commit/75f975e9bea7ce84af58387218f55121794e00e7))
     > work on #24
   

### Bug Fixes

 -  separate thread panel ([4383975f68972b7](https://github.com/bsorrentino/langgraph4j/commit/4383975f68972b768e6f421a9e79bb34c1a49676))
     > work on #24

 -  diagram generator issue with START on mermaid ([7474b86718cbf43](https://github.com/bsorrentino/langgraph4j/commit/7474b86718cbf43d87def78e4beee88b60ea0392))
     > work on #24

 -  mermaid generation ([53b68e0ffef4291](https://github.com/bsorrentino/langgraph4j/commit/53b68e0ffef4291792e13e70d975d3ac662a0aaf))
     > START, END issue
     > work on #24

 -  graph  layout ([ab0a0c2cf91b5be](https://github.com/bsorrentino/langgraph4j/commit/ab0a0c2cf91b5be6959d0c0d40fbeb984838660f))
     > sync container and grapsh svg size
     > work on #24


### Documentation

 -  update readme ([cb01f90346068d6](https://github.com/bsorrentino/langgraph4j/commit/cb01f90346068d667ff6ac2446542f2643b0ed11))

 -  **pom**  add executions comment ([0193aefd115351f](https://github.com/bsorrentino/langgraph4j/commit/0193aefd115351fe0979682b79d87d47fce669df))

 -  update changeme ([e8552308ed95488](https://github.com/bsorrentino/langgraph4j/commit/e8552308ed95488229f765dc23fe0dd3f50c3185))

 -  update changeme ([95b8c4541900bab](https://github.com/bsorrentino/langgraph4j/commit/95b8c4541900bab6b6429506369f5ae5ba0600af))


### Refactor

 -  update model ([1cac3901446ebf5](https://github.com/bsorrentino/langgraph4j/commit/1cac3901446ebf542328a4f8c2946f8f47c2c262))
    > work on #24

 -  **pom**  add hint for server exection ([0e8d7eca3dfdc2e](https://github.com/bsorrentino/langgraph4j/commit/0e8d7eca3dfdc2e8c3efc917a037c6d848d16553))
    > work on #24

 -  **executor.js**  rename property ([bef2f436746b3df](https://github.com/bsorrentino/langgraph4j/commit/bef2f436746b3df4031047e11a7fce884a714536))
    > work on #24

 -  **NodeOutput**  remove lombok @Value and remove final class constraint ([41a095ebffff6b5](https://github.com/bsorrentino/langgraph4j/commit/41a095ebffff6b525151d16a57f1161a10dc3a69))
    > work on #24

 -  make StateSnapshot inherit from NodeOutput ([b210f381e061aa6](https://github.com/bsorrentino/langgraph4j/commit/b210f381e061aa63527bf7285f15493c09c3c29e))
    > work on #24

 -  **server**  enable use of StateGraph ([16aefea482a7aaa](https://github.com/bsorrentino/langgraph4j/commit/16aefea482a7aaa2590232b2160b769566205a77))
    > work on #24

 -  **samples**  expose StateGraph intead of CompiledGraph ([f193da8f1d3f632](https://github.com/bsorrentino/langgraph4j/commit/f193da8f1d3f6326a9011b0c047c29a7877d5513))
    > work on #24

 -  refine set-version script ([26351252b389547](https://github.com/bsorrentino/langgraph4j/commit/26351252b389547d4810b28a74444f13718ab62a))
   

### ALM 

 -  bump to new version ([abdb35503b611a7](https://github.com/bsorrentino/langgraph4j/commit/abdb35503b611a750d12d9ec0ded547cba298942))
   
 -  **forntend**  update dist ([644e7a1c232ec48](https://github.com/bsorrentino/langgraph4j/commit/644e7a1c232ec488c3455f65392384d71a6573b4))
   
 -  update webapp dist ([bebf5c045bd939d](https://github.com/bsorrentino/langgraph4j/commit/bebf5c045bd939d56cdd2b7fd40e3f848b789979))
    > work on #24

 -  bump to SNAPSHOT ([a3eb4e92badd416](https://github.com/bsorrentino/langgraph4j/commit/a3eb4e92badd4163182bf82215f6a00fd362f139))
   
 -  **server**  update frontend dist ([fe4aff4ca05cc04](https://github.com/bsorrentino/langgraph4j/commit/fe4aff4ca05cc04a4c8dd946516428377fd65be9))
   
 -  bump to SNAPSHOT ([ee26478dab44f09](https://github.com/bsorrentino/langgraph4j/commit/ee26478dab44f09da09365f8d1ac41fa9d4172ac))
   
 -  bump deps versions ([963be9b8e824f80](https://github.com/bsorrentino/langgraph4j/commit/963be9b8e824f8066ea1a6629c16f33d390d675c))
    > lit, tailwindcss, postcss, autoprefixer, typescript
 > work on #24

 -  update css gen tools ([8b1b0e2b0224e93](https://github.com/bsorrentino/langgraph4j/commit/8b1b0e2b0224e931233308cad49d6d3204a87c13))
   





<!-- "name: v1.0-20240915" is a release tag -->

## [v1.0-20240915](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20240915) (2024-09-15)





### ALM 

 -  bump new intermediate version ([683a8ab92e407da](https://github.com/bsorrentino/langgraph4j/commit/683a8ab92e407da2f2234d68516a176433bb34b6))
   





<!-- "name: v1.0-20240907" is a release tag -->

## [v1.0-20240907](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20240907) (2024-09-07)

### Features

 *  **howtos**  visualize plantuml image ([3ade1774a4e76b0](https://github.com/bsorrentino/langgraph4j/commit/3ade1774a4e76b04c3f4c4237d9e132ec006c948))
   

### Bug Fixes

 -  **diagram**  update diagram generation ([786cede1ac528b6](https://github.com/bsorrentino/langgraph4j/commit/786cede1ac528b60ad2724a08a7e0d2e2bfe1772))
     > - PlantUML
     > - Mermaid


### Documentation

 -  **CompiledGraph**  javadoc refinements ([9d10c4806e7ff21](https://github.com/bsorrentino/langgraph4j/commit/9d10c4806e7ff2144528e259629b55e5987b1c36))

 -  update changeme ([c709e3800d24ebf](https://github.com/bsorrentino/langgraph4j/commit/c709e3800d24ebf23e9985748556caf7751a18b3))



### ALM 

 -  bump developer version ([b87196100219609](https://github.com/bsorrentino/langgraph4j/commit/b871961002196091a8fbbc56a34ebe24dd37f8b5))
   
 -  add set-version shell ([d8c034974444d65](https://github.com/bsorrentino/langgraph4j/commit/d8c034974444d65215c0ab91d289d92395c99fa8))
   





<!-- "name: v1.0-20240906" is a release tag -->

## [v1.0-20240906](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20240906) (2024-09-06)

### Features

 *  **how-tos**  add loggin support ([27c7afd0ecc609b](https://github.com/bsorrentino/langgraph4j/commit/27c7afd0ecc609bca4dfd18ea214928f9271c0f4))
   
 *  **CompiledGraph**  optimize code ([4e9a763700b9814](https://github.com/bsorrentino/langgraph4j/commit/4e9a763700b9814d7ac444151533c6b3486f521b))
     > minimize cloneState() calls
   


### Documentation

 -  update changeme ([890367f24ce3d27](https://github.com/bsorrentino/langgraph4j/commit/890367f24ce3d27c910a2a6fde8deee79bad2c81))


### Refactor

 -  merge PR #23 ([9e1248b468f1204](https://github.com/bsorrentino/langgraph4j/commit/9e1248b468f1204ebbc83b75c35fac798bb57fec))
   

### ALM 

 -  update git ignore ([1983a7380200aa2](https://github.com/bsorrentino/langgraph4j/commit/1983a7380200aa2ad282a2fef3d5eb090bc78dd7))
   
 -  bump to developer release ([e8a2f07f273fe59](https://github.com/bsorrentino/langgraph4j/commit/e8a2f07f273fe5903a9a39525991e062968732a1))
   





<!-- "name: v1.0-beta4" is a release tag -->

## [v1.0-beta4](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-beta4) (2024-09-06)

### Features

 *  **notebook**  add "How to view and update past graph state" ([ae60f3094661e4e](https://github.com/bsorrentino/langgraph4j/commit/ae60f3094661e4e2bfa5d9000bb11ec532d51bd4))
   

### Bug Fixes

 -  pause management ([9ec77fb711d11c3](https://github.com/bsorrentino/langgraph4j/commit/9ec77fb711d11c336e8ad26d3002f9e7b6868960))
     > - check resume startpoint
     > - refine state cloning
     > - improve unit test
     > work on #14


### Documentation

 -  update readme ([67b61a7be81bd11](https://github.com/bsorrentino/langgraph4j/commit/67b61a7be81bd113fce703a8e48f429722b34ffc))

 -  **time-travel.ipynb**  update ([a1216b97240b6b9](https://github.com/bsorrentino/langgraph4j/commit/a1216b97240b6b9f34bb188aae54b10ae5fe964d))

 -  update maven site ([c89323fe75721c6](https://github.com/bsorrentino/langgraph4j/commit/c89323fe75721c68d81ece1e860b8255dda77fef))

 -  update site ([79fb38b1da3ba77](https://github.com/bsorrentino/langgraph4j/commit/79fb38b1da3ba775e33e8436a5fee9499de0e2bb))

 -  update changeme ([a6c54e814b4b9ef](https://github.com/bsorrentino/langgraph4j/commit/a6c54e814b4b9ef747eb13f37f6b1e7f27730cb4))

 -  add "How to view and update past graph state" ([40dd70e59c6d283](https://github.com/bsorrentino/langgraph4j/commit/40dd70e59c6d2833a2b12094d02d4ebf82de120e))

 -  update changeme ([8adee9a867c7de7](https://github.com/bsorrentino/langgraph4j/commit/8adee9a867c7de79c25824ffd9ce1b8acd61164e))


### Refactor

 -  update git ignore ([a09be23ce723385](https://github.com/bsorrentino/langgraph4j/commit/a09be23ce72338550dac754809bdabb3215020b3))
   
 -  **TryConsumer**  add logging ([7023501ea4ea944](https://github.com/bsorrentino/langgraph4j/commit/7023501ea4ea944e99c9d4878a93de1dc2b8674e))
   

### ALM 

 -  bump new beta version ([25f45aa52fc1f38](https://github.com/bsorrentino/langgraph4j/commit/25f45aa52fc1f3817954e7e2626d88d6ac400c99))
   
 -  utility shells refinements ([f1765769556e024](https://github.com/bsorrentino/langgraph4j/commit/f1765769556e0242a96e1abbe6371e423cb8731b))
   
 -  bump langchai4j version ([b0689ac981193b6](https://github.com/bsorrentino/langgraph4j/commit/b0689ac981193b6cb42432410180f9cb1a55fd6f))
   





<!-- "name: v1.0-20240905" is a release tag -->

## [v1.0-20240905](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20240905) (2024-09-05)

### Features

 *  **notebook**  add "How to view and update past graph state" ([8df2d1101fa898e](https://github.com/bsorrentino/langgraph4j/commit/8df2d1101fa898ea828da0db3791fce6a11ff890))
   

### Bug Fixes

 -  pause management ([7042bce11521c91](https://github.com/bsorrentino/langgraph4j/commit/7042bce11521c9145e6c923da24497a74f245fe6))
     > - check resume startpoint
     > - refine state cloning
     > - improve unit test


### Documentation

 -  add "How to view and update past graph state" ([99d9130895e0b0a](https://github.com/bsorrentino/langgraph4j/commit/99d9130895e0b0aa62b71565511a7eef5c3461db))

 -  update changeme ([609cc0f20f03f88](https://github.com/bsorrentino/langgraph4j/commit/609cc0f20f03f88d45ba9e481aff27126d78c669))


### Refactor

 -  **TryConsumer**  add logging ([7c752de608c863e](https://github.com/bsorrentino/langgraph4j/commit/7c752de608c863e0ab26928923dac01e4d2c273c))
   
 -  update git ignore ([3eaabbeef0c25be](https://github.com/bsorrentino/langgraph4j/commit/3eaabbeef0c25be559cc208d13b1bcbc69d31796))
   
 -  **serializer**  remove CheckpointSerializer ([26128e081863ea2](https://github.com/bsorrentino/langgraph4j/commit/26128e081863ea259403c0e39c5b06f787ce4c77))
   
 -  **serializer**  remove type() method ([ebbac63139e2e9f](https://github.com/bsorrentino/langgraph4j/commit/ebbac63139e2e9fa2834787564a22dd7c1ad25e9))
    > Simplify implementation


### ALM 

 -  move to next dev release ([e8b0735d16687bf](https://github.com/bsorrentino/langgraph4j/commit/e8b0735d16687bfe7922dbcea2095e90c42dc8d9))
   





<!-- "name: v1.0-beta3" is a release tag -->

## [v1.0-beta3](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-beta3) (2024-09-03)

### Features

 *  **serilaizer**  enhance the serialization api ([06ed83d7c8ba6c8](https://github.com/bsorrentino/langgraph4j/commit/06ed83d7c8ba6c8360e60f0cada7a3a124a230c5))
     > - add BaseSerializer
     > - add Custom Serializer support
   
 *  **notebook**  add persistence how-to using a java notebook ([b89dbcfe05123b8](https://github.com/bsorrentino/langgraph4j/commit/b89dbcfe05123b8459c26a95d663608177ed91d3))
   
 *  **persistence.ipynb**  add AiMessage Serializer ([363600c0b629d42](https://github.com/bsorrentino/langgraph4j/commit/363600c0b629d423bc70dd9e577a99290b3b417b))
   
 *  **serializer**  add custom serializer support ([f958f0c03e5b9c8](https://github.com/bsorrentino/langgraph4j/commit/f958f0c03e5b9c816188aa927b82cc3a7ae754a5))
   
 *  **persistence.ipynb**  add workflow execution ([c4bf0819b3f65a2](https://github.com/bsorrentino/langgraph4j/commit/c4bf0819b3f65a2c94c4bc2e715b251bf0aedc1a))
   
 *  **persistence**  refine Graph definition ([f09aeb6a0b583b4](https://github.com/bsorrentino/langgraph4j/commit/f09aeb6a0b583b4c1c620978772371a331e79cce))
   
 *  add support of java notebook based on 'rapaio-jupyter-kernel' ([41ab46694adf90e](https://github.com/bsorrentino/langgraph4j/commit/41ab46694adf90e9853d5e53f4acd0f89fa650ff))
   
 *  add factory methods to create custom Channel ([8abb17312f788fb](https://github.com/bsorrentino/langgraph4j/commit/8abb17312f788fb2b510145e22d35f31c2a96c01))
   

### Bug Fixes

 -  **CompiledGraph**  when asNode is provided next node is evaluated keeping in consideration edges ([d3595cb41ec9f64](https://github.com/bsorrentino/langgraph4j/commit/d3595cb41ec9f64ce9e2f2806e564a2a94ef67c3))
     > work on #14


### Documentation

 -  **site**  update documentation ([254f64f7bfb41a1](https://github.com/bsorrentino/langgraph4j/commit/254f64f7bfb41a14da82aba7ea4e4af67ef42c11))
     > - persistence howto

 -  update links ([0d7b1df3766efe4](https://github.com/bsorrentino/langgraph4j/commit/0d7b1df3766efe4d7901ce029e6fc77f597e06f6))
     > work on #12

 -  update links ([a5af831f480b016](https://github.com/bsorrentino/langgraph4j/commit/a5af831f480b016540e489e71126efb6818cc3b0))
     > work on #12

 -  update links ([2a1f1b8ca779832](https://github.com/bsorrentino/langgraph4j/commit/2a1f1b8ca779832ff7f0329b3763840fc8373c7d))
     > work on #12

 -  update links ([45eb6850811a164](https://github.com/bsorrentino/langgraph4j/commit/45eb6850811a164ffa6518448c76dbf082569024))

 -  update links ([7245d150fed8c1d](https://github.com/bsorrentino/langgraph4j/commit/7245d150fed8c1df07a3e14e65c9a3b9f1257979))

 -  update links ([6c77da53ac2fdc7](https://github.com/bsorrentino/langgraph4j/commit/6c77da53ac2fdc7a8867f90ca71a85a30e1fa8fb))

 -  update links ([4449fb4148d5a90](https://github.com/bsorrentino/langgraph4j/commit/4449fb4148d5a907a2d45319362928b5c1dbb9a7))

 -  update ([b82abebd61df183](https://github.com/bsorrentino/langgraph4j/commit/b82abebd61df183930c8ea288d4a31c61a974bcd))

 -  update ([ea080158b19a91d](https://github.com/bsorrentino/langgraph4j/commit/ea080158b19a91dbb0b56e6563895e7006e9123a))

 -  add javadoc task on site  generation ([c236773dbef7752](https://github.com/bsorrentino/langgraph4j/commit/c236773dbef77520d1471b6c785725e75017ac30))
     > work on #12

 -  add Conceptual Guides ([33ba7527b9d2c79](https://github.com/bsorrentino/langgraph4j/commit/33ba7527b9d2c79f9154f3afa6fdce8289f8cd30))
     > work on #12

 -  update readme ([1a087fca04acfc6](https://github.com/bsorrentino/langgraph4j/commit/1a087fca04acfc65740befdbedd97250794fd884))

 -  update changeme ([04f1ff7a00254f3](https://github.com/bsorrentino/langgraph4j/commit/04f1ff7a00254f31520653cc07ebec84247ed9b6))

 -  update changeme ([d8bdbcb7309dde5](https://github.com/bsorrentino/langgraph4j/commit/d8bdbcb7309dde548bce27aa97ed0fec02f65287))

 -  update changeme ([160d7093b6123ec](https://github.com/bsorrentino/langgraph4j/commit/160d7093b6123ec645bcbe63e4830d70c6c6b297))


### Refactor

 -  **TryConsumer**  add log ([526f296c70bc587](https://github.com/bsorrentino/langgraph4j/commit/526f296c70bc5872c4dfb58f4ea27146350a0794))
   
 -  remove unused import ([eeb00a08078449c](https://github.com/bsorrentino/langgraph4j/commit/eeb00a08078449cf9e6036ac940bf2ac59e69783))
   
 -  add @@FunctionalInterface annotation ([e37621cf6bdbef0](https://github.com/bsorrentino/langgraph4j/commit/e37621cf6bdbef0782c420be75bd8790d516e930))
   

### ALM 

 -  bump version 1.0-beta3 ([d7fbde9e3a6f8d5](https://github.com/bsorrentino/langgraph4j/commit/d7fbde9e3a6f8d5d2f549b153cd54a30f5c15eea))
   





<!-- "name: v1.0-20240828" is a release tag -->

## [v1.0-20240828](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-20240828) (2024-08-28)

### Features

 *  add support for 'interruptBeforereak' and 'interruptAfter' ([3083d9d95d05393](https://github.com/bsorrentino/langgraph4j/commit/3083d9d95d05393ac651579431b39935a597df94))
   
 *  **MemorySaver**  add rw lock to memory saver ([a00054b2169e348](https://github.com/bsorrentino/langgraph4j/commit/a00054b2169e348b0db4f5c5702d05e224a57a35))
   
 *  **CompiledGraph**  expose state Management ([183a0ceb7069f1f](https://github.com/bsorrentino/langgraph4j/commit/183a0ceb7069f1f96b2e69590843c9ed08f7818b))
     > - getState
     > - getStateHistory
     > - updateState
     > - update streaming processing to support graph resume
     > work on #14
   
 *  **StateSnapshot**  add support StateSnaphot object ([cc86564cc2f2759](https://github.com/bsorrentino/langgraph4j/commit/cc86564cc2f27595559018635e6af48117b28275))
     > work on #14
   
 *  **RunnableConfig**  add checkpointId and nextNode properties ([509d7ebd50eef7a](https://github.com/bsorrentino/langgraph4j/commit/509d7ebd50eef7aff6ba74d4865645a738de44a1))
     > work on #14 #20
   
 *  **Checkpoint**  store the nextNodeId ([6e1ca60de4572af](https://github.com/bsorrentino/langgraph4j/commit/6e1ca60de4572afa7b28f00daf5fec3d78d6c55d))
     > work on #14
   
 *  **BaseCheckpointSaver**  add support for ThreadId ([05c293faa56cfad](https://github.com/bsorrentino/langgraph4j/commit/05c293faa56cfad8c3f94bb8f69bc74731f65e25))
     > work on #20
   
 *  add MapSerializer ([1407b41f8d412eb](https://github.com/bsorrentino/langgraph4j/commit/1407b41f8d412ebb7337764f764be6365ee12521))
   


### Documentation

 -  update changeme ([2a47f9f0edca129](https://github.com/bsorrentino/langgraph4j/commit/2a47f9f0edca1293683159758e386932b9c109c9))

 -  update changeme ([5e3259ec27d6aca](https://github.com/bsorrentino/langgraph4j/commit/5e3259ec27d6aca32af698d47b11a58ffce12119))

 -  update changeme ([8d965e7b07df3fd](https://github.com/bsorrentino/langgraph4j/commit/8d965e7b07df3fd14312e86e08adf33015844ce6))


### Refactor

 -  rename getCheckpointSaver() to checkpointSaver() because it returns an Optional ([fd072d239a0e7f8](https://github.com/bsorrentino/langgraph4j/commit/fd072d239a0e7f8a4d5e5068b87eef31b4fedce7))
   
 -  make TryConsumer public ([2447a2380ffcc40](https://github.com/bsorrentino/langgraph4j/commit/2447a2380ffcc40a02141fe471d8c7ad738b6329))
   
 -  make TryConsumer public ([3f92e4c7c3b8243](https://github.com/bsorrentino/langgraph4j/commit/3f92e4c7c3b824339f9720068a037189db1d6287))
   
 -  **CompiledGraph**  refine state management ([c2a8e876dbc0342](https://github.com/bsorrentino/langgraph4j/commit/c2a8e876dbc0342c37331b097e68b54d5f9f6fe2))
    > work on #14

 -  **StateSnapshot**  delegate next to checkpoint ([8199a0d7a039192](https://github.com/bsorrentino/langgraph4j/commit/8199a0d7a039192daeaeee4bbdf2149967c20913))
    > work on #14

 -  **Checkpoint**  move from AgentState to Map<K,V> ([fb742ac09bdf0db](https://github.com/bsorrentino/langgraph4j/commit/fb742ac09bdf0db170305ca3e2202b766a2e8823))
   
 -  **AgentState**  add updateState utility methods ([af0bd50092583e2](https://github.com/bsorrentino/langgraph4j/commit/af0bd50092583e2fa8da987b935fb4a929ca7e78))
   
 -  rename InvokeConfig to RunnableConfig ([40a910f32dd882c](https://github.com/bsorrentino/langgraph4j/commit/40a910f32dd882c7c7168cc326311861a6ef58e6))
    > work on #20

 -  rename InvokeConfig to RunnableConfig ([fe9ff015241ef0d](https://github.com/bsorrentino/langgraph4j/commit/fe9ff015241ef0dc88e10e36b605be01fcab589b))
    > work on #20


### ALM 

 -  move to next developer version ([5a7fa556b33613c](https://github.com/bsorrentino/langgraph4j/commit/5a7fa556b33613cd2e1d7c3287baa4e0da2d1f78))
   
 -  add changelog update shell ([cc44dfea071d6f7](https://github.com/bsorrentino/langgraph4j/commit/cc44dfea071d6f7333ebe68d32a41142a8b2a2b5))
   





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
   



