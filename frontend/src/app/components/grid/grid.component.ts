import { Component, Input } from '@angular/core';
import { LineComponent } from './line/line.component';
import { NgForOf } from '@angular/common';
@Component({
  selector: 'grid',
  templateUrl: './grid.component.html',
  styleUrl: './grid.component.css',
  standalone: true,
  imports: [NgForOf, LineComponent],
})
export class GridComponent {
  @Input() columns = 0;
  @Input() rows = 0;
  protected readonly Array = Array;
}
