import { Component, Input } from '@angular/core';
import { NgForOf, NgIf } from '@angular/common';
import { CellComponent } from './cell/cell.component';

@Component({
  standalone: true,
  selector: 'line',
  templateUrl: `./line.component.html`,
  styleUrl: './line.component.css',
  imports: [NgForOf, CellComponent, NgIf],
})
export class LineComponent {
  @Input() length = 0;
  @Input() lineIndex = 0;
  protected readonly Array = Array;
}
