import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnimalModifyComponent } from './animal-modify.component';

describe('AnimalModifyComponent', () => {
  let component: AnimalModifyComponent;
  let fixture: ComponentFixture<AnimalModifyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AnimalModifyComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AnimalModifyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
