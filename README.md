# Wireworld

A wireworld is made up of four types of cell.

* ![][1] Empty
* ![][2] Wire
* ![][3] Electron Head
* ![][4] Electron Tail

## Rules
After each tick:

* ![][1] always stays ![][1]
* ![][3] always becomes ![][4]
* ![][4] always becomes ![2]
* If ![][2] has 1 or 2 ![][3] neighbours, it becomes ![][3]
  * Otherwise it stays as ![][2]

## Keys
| Toggle play/pause | Paint at cursor | Clear grid |
| ----------------- | --------------- | ---------- |
| `enter`           | `space`         | `x`        |

## Cursor
| Move left | Move down | Move up | Move right |
| --------- | --------- | ------- | ---------- |
| `←` or `h` |  `↓` or `j` |  `↑` or `k` |  `→` or `l` |

## Tools
| Paint with ![][1] | Paint with ![][2] | Paint with ![][3] | Paint with ![][4] |
| ----------------- | ----------------- | ----------------- | ----------------- |
| `1`               | `2`               | `3`               | `4`               |

## Selection
| Selection mode | Copy selection | Paste selection |
| -------------- | -------------- | --------------- |
| `ctrl`         | `y`            | `p`             |

## License
Distributed under the Eclipse Public License version 1.0.

[1]: resources/public/img/empty.png
[2]: resources/public/img/wire.png
[3]: resources/public/img/head.png
[4]: resources/public/img/tail.png

