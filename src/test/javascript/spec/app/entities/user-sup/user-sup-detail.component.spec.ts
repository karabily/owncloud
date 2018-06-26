/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { OurcloudTestModule } from '../../../test.module';
import { UserSupDetailComponent } from '../../../../../../main/webapp/app/entities/user-sup/user-sup-detail.component';
import { UserSupService } from '../../../../../../main/webapp/app/entities/user-sup/user-sup.service';
import { UserSup } from '../../../../../../main/webapp/app/entities/user-sup/user-sup.model';

describe('Component Tests', () => {

    describe('UserSup Management Detail Component', () => {
        let comp: UserSupDetailComponent;
        let fixture: ComponentFixture<UserSupDetailComponent>;
        let service: UserSupService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [OurcloudTestModule],
                declarations: [UserSupDetailComponent],
                providers: [
                    UserSupService
                ]
            })
            .overrideTemplate(UserSupDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(UserSupDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserSupService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new UserSup(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.userSup).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
