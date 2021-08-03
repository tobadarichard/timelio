import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { EmploiTemps } from 'src/app/model/emploi-temps';
import { EmploiService } from 'src/app/services/emploi/emploi.service';

@Component({
  selector: 'app-emploi',
  templateUrl: './emploi.component.html',
  styleUrls: ['./emploi.component.css']
})
export class EmploiComponent implements OnInit {
  isUserEmploi = false;
  hasError = false;
  emploi: EmploiTemps | null = null;
  constructor(private route: ActivatedRoute,
    private emploiService: EmploiService) { }

  ngOnInit(): void {
    var code = this.route.snapshot.paramMap.get('code') || '';
    var id = Number(this.route.snapshot.paramMap.get('id')) || 0;
    this.isUserEmploi = code == '';

    (this.isUserEmploi ? this.emploiService.getUserEmploi(id) : this.emploiService.getEmploi(code))
      .subscribe((emploi) => {this.emploi = emploi;}, () => {this.hasError = true;});
  }
}
