import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { UserSupComponent } from './user-sup.component';
import { UserSupDetailComponent } from './user-sup-detail.component';
import { UserSupPopupComponent } from './user-sup-dialog.component';
import { UserSupDeletePopupComponent } from './user-sup-delete-dialog.component';

@Injectable()
export class UserSupResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const userSupRoute: Routes = [
    {
        path: 'user-sup',
        component: UserSupComponent,
        resolve: {
            'pagingParams': UserSupResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ourcloudApp.userSup.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'user-sup/:id',
        component: UserSupDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ourcloudApp.userSup.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const userSupPopupRoute: Routes = [
    {
        path: 'user-sup-new',
        component: UserSupPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ourcloudApp.userSup.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'user-sup/:id/edit',
        component: UserSupPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ourcloudApp.userSup.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'user-sup/:id/delete',
        component: UserSupDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ourcloudApp.userSup.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
