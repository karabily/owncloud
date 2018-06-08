import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { OurcloudSharedModule } from '../../shared';
import { OurcloudAdminModule } from '../../admin/admin.module';
import {
    JobService,
    JobPopupService,
    JobComponent,
    JobDetailComponent,
    JobDialogComponent,
    JobPopupComponent,
    JobDeletePopupComponent,
    JobDeleteDialogComponent,
    jobRoute,
    jobPopupRoute,
    JobResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...jobRoute,
    ...jobPopupRoute,
];

@NgModule({
    imports: [
        OurcloudSharedModule,
        OurcloudAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        JobComponent,
        JobDetailComponent,
        JobDialogComponent,
        JobDeleteDialogComponent,
        JobPopupComponent,
        JobDeletePopupComponent,
    ],
    entryComponents: [
        JobComponent,
        JobDialogComponent,
        JobPopupComponent,
        JobDeleteDialogComponent,
        JobDeletePopupComponent,
    ],
    providers: [
        JobService,
        JobPopupService,
        JobResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OurcloudJobModule {}
