/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { OurcloudTestModule } from '../../../test.module';
import { UserSupComponent } from '../../../../../../main/webapp/app/entities/user-sup/user-sup.component';
import { UserSupService } from '../../../../../../main/webapp/app/entities/user-sup/user-sup.service';
import { UserSup } from '../../../../../../main/webapp/app/entities/user-sup/user-sup.model';

describe('Component Tests', () => {

    describe('UserSup Management Component', () => {
        let comp: UserSupComponent;
        let fixture: ComponentFixture<UserSupComponent>;
        let service: UserSupService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [OurcloudTestModule],
                declarations: [UserSupComponent],
                providers: [
                    UserSupService
                ]
            })
            .overrideTemplate(UserSupComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(UserSupComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserSupService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new UserSup(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.userSups[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
