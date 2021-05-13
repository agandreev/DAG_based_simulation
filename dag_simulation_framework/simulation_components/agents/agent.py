import numpy as np
from typing import List


class Agent:
    def __init__(self, id: int):
        self.id: int = id
        self.existing_transactions: List = list()
        self.not_existing_transaction: List = list()
        self.color: str = "#%06x" % np.random.randint(0, 0xFFFFFF)

    def clear_transactions_lists(self) -> None:
        self.existing_transactions: List = list()
        self.not_existing_transaction: List = list()

    def get_legend(self) -> (str, str):
        return str(self), self.color

    def __str__(self) -> str:
        return f"Agent №{self.id}"

    def __repr__(self) -> str:
        return f"Agent №{self.id}"
