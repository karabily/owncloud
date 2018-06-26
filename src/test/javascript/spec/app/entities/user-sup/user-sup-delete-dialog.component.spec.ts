/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { OurcloudTestModule } from '../../../test.module';
import { UserSupDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/user-sup/user-sup-delete-dialog.component';
import { UserSupService } from '../../../../../../main/webapp/app/entities/user-sup/user-sup.service';

describe('Component Tests', () => {

    describe('UserSup Management Delete Component', () => {
        let comp: UserSupDeleteDialogComponent;
        let fixture: ComponentFixture<UserSupDeleteDialogComponent>;
        let service: UserSupService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [OurcloudTestModule],
                declarations: [UserSupDeleteDialogComponent],
                providers: [
                    UserSupService
                ]
            })
            .overrideTemplate(UserSupDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(UserSupDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserSupService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
