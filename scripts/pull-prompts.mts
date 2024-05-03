import * as hub from 'langchain/hub';
import { JsonMarkdownStructuredOutputParser } from "@langchain/core/output_parsers";
import { PromptTemplate } from '@langchain/core/prompts';
import 'zx/globals'
import { diagramSchema } from './schema.mts'

class Prompt {
    
    constructor( public name: string ){}

    async save( outDir: string ) {
        const p = await hub.pull<PromptTemplate>(`bsorrentino/${this.name}`);
        let content = p.template as string
        content = content.replace(/{\s*(.*?)\s*}/g, '{{$1}}');
        await fs.writeFile( path.join( outDir, `${this.name}.txt`), content )
    }
}

class PromptWithSchema extends Prompt {

    async save( outDir: string ) {
        const promptTemplate = await hub.pull<PromptTemplate>(`bsorrentino/${this.name}`);

        const outputParser = JsonMarkdownStructuredOutputParser.fromZodSchema( diagramSchema);
        let content = await promptTemplate.format({ format_instructions: outputParser.getFormatInstructions() });
        await fs.writeFile( path.join( outDir, `${this.name}.txt`), content )
    }
}

[
    new Prompt( 'convert_generic_diagram_to_plantuml' ),
    new Prompt( 'convert_sequence_diagram_to_plantuml' ),
    new PromptWithSchema( 'describe_diagram_image' )
].forEach( async p => {
    const outDir = path.join( '..', 'agents-jdk8', 'src', 'main', 'java', 'resources' )

    try {
        await p.save( outDir )
    }
    catch( e ) {
        console.error( `ERROR SAVING ${p.name}`, e)
    }

})

