import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { OurcloudSharedModule } from '../../shared';
import { OurcloudAdminModule } from '../../admin/admin.module';
import {
    UserSupService,
    UserSupPopupService,
    UserSupComponent,
    UserSupDetailComponent,
    UserSupDialogComponent,
    UserSupPopupComponent,
    UserSupDeletePopupComponent,
    UserSupDeleteDialogComponent,
    userSupRoute,
    userSupPopupRoute,
    UserSupResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...userSupRoute,
    ...userSupPopupRoute,
];

@NgModule({
    imports: [
        OurcloudSharedModule,
        OurcloudAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        UserSupComponent,
        UserSupDetailComponent,
        UserSupDialogComponent,
        UserSupDeleteDialogComponent,
        UserSupPopupComponent,
        UserSupDeletePopupComponent,
    ],
    entryComponents: [
        UserSupComponent,
        UserSupDialogComponent,
        UserSupPopupComponent,
        UserSupDeleteDialogComponent,
        UserSupDeletePopupComponent,
    ],
    providers: [
        UserSupService,
        UserSupPopupService,
        UserSupResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OurcloudUserSupModule {}
