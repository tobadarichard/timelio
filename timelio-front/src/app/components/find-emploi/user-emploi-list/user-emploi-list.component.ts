import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { EmploiTemps } from 'src/app/model/emploi-temps';
import { Page } from 'src/app/model/page';
import { EmploiService } from 'src/app/services/emploi/emploi.service';

@Component({
  selector: 'app-user-emploi-list',
  templateUrl: './user-emploi-list.component.html',
  styleUrls: ['./user-emploi-list.component.css']
})
export class UserEmploiListComponent implements OnInit {
  page = 1;
  hasError = false;
  currentPage: Page<EmploiTemps> | null = null;
  constructor(private emploiService: EmploiService) { }

  ngOnInit(): void {
    this.emploiService.getUserEmploiPage(0).subscribe((page) => {this.currentPage = page;},
    () => {this.hasError = true;});
  }

  previousPage(): void{
    this.emploiService.getUserEmploiPage(this.page-2).subscribe((page) => {
      this.page = this.page -1;
      this.currentPage = page;
    },
    () => {this.hasError = true;});
  }

  nextPage(): void{
    this.emploiService.getUserEmploiPage(this.page).subscribe((page) => {
      this.page = this.page + 1;
      this.currentPage = page;
    },
    () => {this.hasError = true;});
  }
}
