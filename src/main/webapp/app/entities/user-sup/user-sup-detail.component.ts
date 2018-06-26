import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { UserSup } from './user-sup.model';
import { UserSupService } from './user-sup.service';

@Component({
    selector: 'jhi-user-sup-detail',
    templateUrl: './user-sup-detail.component.html'
})
export class UserSupDetailComponent implements OnInit, OnDestroy {

    userSup: UserSup;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private userSupService: UserSupService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInUserSups();
    }

    load(id) {
        this.userSupService.find(id)
            .subscribe((userSupResponse: HttpResponse<UserSup>) => {
                this.userSup = userSupResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInUserSups() {
        this.eventSubscriber = this.eventManager.subscribe(
            'userSupListModification',
            (response) => this.load(this.userSup.id)
        );
    }
}
