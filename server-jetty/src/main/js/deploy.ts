#!/usr/bin/env bun
import { $ } from "bun";

$.nothrow();
await $`bun build.ts`
await $`bun twgen.ts --no-watch`
await $`bun build.ts`
await $`rm ../resources/webapp/*`
await $`cp dist/* ../resources/webapp`