import { $ } from "bun";

$.nothrow();
await $`rm -r .parcel-cache`
await $`rm -r dist`

await $`bun run build`
await $`rm ../webapp/*`
await $`cp dist/* ../webapp`