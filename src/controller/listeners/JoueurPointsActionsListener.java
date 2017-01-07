package controller.listeners;

import java.util.Map;

import models.enums.Origine;

public interface JoueurPointsActionsListener {
	public void updatePointsActions(Map<Origine,Integer> pointsAction);
}
