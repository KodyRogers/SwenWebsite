import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NeedBasketComponent } from './need-basket.component';

describe('NeedBasketComponent', () => {
  let component: NeedBasketComponent;
  let fixture: ComponentFixture<NeedBasketComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NeedBasketComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NeedBasketComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
