from simulation_components.agents.agent import Agent
from typing import Dict


class Transaction:
    def __init__(self, id: int, time: float):
        self.id: int = id
        self.time: float = time
        self.owner: 'Agent' = None
        self.weight_for_each_agent: Dict['Agent', int] = dict()

    def __repr__(self) -> str:
        return f"{self.id}"#\n{self.weight_for_each_agent}\n"

    def __str__(self) -> str:
        return f"{self.id}"
