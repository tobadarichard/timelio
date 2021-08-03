import { Component, Input, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { EmploiTemps } from 'src/app/model/emploi-temps';
import { EmploiService } from 'src/app/services/emploi/emploi.service';

@Component({
  selector: 'app-delete-emploi-modal',
  templateUrl: './delete-emploi-modal.component.html',
  styleUrls: ['./delete-emploi-modal.component.css']
})
export class DeleteEmploiModalComponent implements OnInit {
  @Input()
  emploi!: EmploiTemps;
  @Input()
  isUserEmploi!: boolean;
  constructor(private emploiService: EmploiService,private router: Router,
    private snackbar: MatSnackBar) { }

  ngOnInit(): void {
  }

  deleteEmploi(): void{
    (this.isUserEmploi ? 
      this.emploiService.deleteUserEmploi(this.emploi.id) 
      : this.emploiService.deleteEmploi(this.emploi.codeAcces)).subscribe(() => {
        this.snackbar.open('Emploi du temps supprimé');
        this.router.navigate(['emplois']);
      }, () => {this.snackbar.open('Une erreur est survenue')});
  }

}
