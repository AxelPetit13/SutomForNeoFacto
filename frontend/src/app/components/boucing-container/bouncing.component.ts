import { Component, ElementRef } from '@angular/core';
import gsap from 'gsap';

@Component({
  selector: 'bouncing-container',
  template: `<ng-content></ng-content>`,
  standalone: true,
})
export class BouncingComponent {
  constructor(private elementRef: ElementRef) {}
  ngOnInit() {
    const xTo = gsap.quickTo(this.elementRef.nativeElement, 'x', {
      duration: 1,
      ease: 'elastic.out(1, 0.3)',
    });
    const yTo = gsap.quickTo(this.elementRef.nativeElement, 'y', {
      duration: 1,
      ease: 'elastic.out(1, 0.3)',
    });

    this.elementRef.nativeElement.addEventListener(
      'mousemove',
      (e: MouseEvent) => {
        const { clientX, clientY } = e;
        const { height, width, left, top } =
          this.elementRef.nativeElement.getBoundingClientRect();
        const x = clientX - (left + width / 2);
        const y = clientY - (top + height / 2);
        xTo(x);
        yTo(y);
      },
    );
    this.elementRef.nativeElement.addEventListener(
      'mouseleave',
      (e: MouseEvent) => {
        xTo(0);
        yTo(0);
      },
    );
  }
}
