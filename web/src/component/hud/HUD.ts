import { Component } from '../Component';

export abstract class HUD extends Component {
  static readonly HEIGHT = 80;

  constructor(width: number, height: number) {
    super(width, height);
  }
}
