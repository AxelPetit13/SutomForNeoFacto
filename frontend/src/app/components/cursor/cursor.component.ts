import { Component, ElementRef } from '@angular/core';

@Component({
  standalone: true,
  selector: 'custom-cursor',
  template: ` <div class="custom-cursor"></div> `,
  styleUrl: './cursor.component.css',
})
export class CursorComponent {
  constructor(private elementRef: ElementRef) {}

  ngOnInit() {
    const cursor =
      this.elementRef.nativeElement.getElementsByClassName('custom-cursor')[0];
    document.addEventListener('mousemove', (e: MouseEvent) => {
      const { clientX, clientY } = e;
      cursor.style.left = `${clientX}px`;
      cursor.style.top = `${clientY}px`;
    });
  }
}
