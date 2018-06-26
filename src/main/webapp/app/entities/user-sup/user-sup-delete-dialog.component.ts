import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { UserSup } from './user-sup.model';
import { UserSupPopupService } from './user-sup-popup.service';
import { UserSupService } from './user-sup.service';

@Component({
    selector: 'jhi-user-sup-delete-dialog',
    templateUrl: './user-sup-delete-dialog.component.html'
})
export class UserSupDeleteDialogComponent {

    userSup: UserSup;

    constructor(
        private userSupService: UserSupService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.userSupService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'userSupListModification',
                content: 'Deleted an userSup'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-user-sup-delete-popup',
    template: ''
})
export class UserSupDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private userSupPopupService: UserSupPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.userSupPopupService
                .open(UserSupDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
