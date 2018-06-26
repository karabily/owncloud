import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { OurcloudJobModule } from './job/job.module';
import { OurcloudUserSupModule } from './user-sup/user-sup.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        OurcloudJobModule,
        OurcloudUserSupModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OurcloudEntityModule {}
