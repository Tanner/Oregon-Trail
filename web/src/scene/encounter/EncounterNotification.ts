import { Notification } from "../../model/Notification";

export class EncounterNotification {
  private notification: Notification;
  private sceneID: string | null;

  constructor(notification: Notification, sceneID: string | null) {
    this.notification = notification;
    this.sceneID = sceneID;
  }

  getNotification(): Notification {
    return this.notification;
  }

  getSceneID(): string | null {
    return this.sceneID;
  }
}
