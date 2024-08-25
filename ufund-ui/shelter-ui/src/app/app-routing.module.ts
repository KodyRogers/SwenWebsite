import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AnimalsComponent } from './animals/animals.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AnimalDetailComponent } from './animal-detail/animal-detail.component';
import { LoginComponent } from './login/login.component'
import { NeedBasketComponent } from './need-basket/need-basket.component'
import { AnimalModifyComponent } from './animal-modify/animal-modify.component';

import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { User } from './user';
import { AdminComponent } from './admin/admin.component';
import { UserComponent } from './user/user.component';

function checkLogin (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
  const usr = JSON.parse(localStorage.getItem("currentUserLogin") || "") as User;
  if (usr.password == "") {
    window.location.replace("http://localhost:4200/login");
  }
}

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'animals', component: AnimalsComponent },
  { path: 'dashboard', component: DashboardComponent, },
  { path: 'admin', component: AdminComponent},
  { path: 'detail/:id', component: AnimalDetailComponent },
  { path: 'user/:name', component: UserComponent},
  { path: 'basket', component: NeedBasketComponent },
  { path: 'login', component: LoginComponent },
  { path: 'modify/:id', component: AnimalModifyComponent},
];

routes.slice(1).forEach(r => {
  if (r.path != "login") {
    r.canActivate = [checkLogin];
  }
});

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}