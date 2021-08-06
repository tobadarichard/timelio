import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-find-emploi',
  templateUrl: './find-emploi.component.html',
  styleUrls: ['./find-emploi.component.css']
})
export class FindEmploiComponent implements OnInit {
  code = '';
  showEmplois = false;
  constructor(public authService: AuthService,private router: Router) { }

  ngOnInit(): void {
  }

  searchEmploi(): void{
    this.code = this.code.trim();
    if (this.code.length > 0){
      this.router.navigate(['/emplois/public',this.code]);
    }
  }

}
