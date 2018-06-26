import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { UserSup } from './user-sup.model';
import { UserSupPopupService } from './user-sup-popup.service';
import { UserSupService } from './user-sup.service';
import { User, UserService } from '../../shared';

@Component({
    selector: 'jhi-user-sup-dialog',
    templateUrl: './user-sup-dialog.component.html'
})
export class UserSupDialogComponent implements OnInit {

    userSup: UserSup;
    isSaving: boolean;

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private userSupService: UserSupService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.userSup.id !== undefined) {
            this.subscribeToSaveResponse(
                this.userSupService.update(this.userSup));
        } else {
            this.subscribeToSaveResponse(
                this.userSupService.create(this.userSup));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<UserSup>>) {
        result.subscribe((res: HttpResponse<UserSup>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: UserSup) {
        this.eventManager.broadcast({ name: 'userSupListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-user-sup-popup',
    template: ''
})
export class UserSupPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private userSupPopupService: UserSupPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.userSupPopupService
                    .open(UserSupDialogComponent as Component, params['id']);
            } else {
                this.userSupPopupService
                    .open(UserSupDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
