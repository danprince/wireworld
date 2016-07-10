# Wireworld

A wireworld is made up of four types of cell.

* ![][1] Empty
* ![][2] Wire
* ![][2] Electron Head
* ![][2] Electron Tail

## Rules
After each tick:

* ![][1] always stays ![][1]
* ![][3] always becomes ![][4]
* ![][4] always becomes ![2]
* If ![][2] has 1 or 2 ![][3] neighbours, it becomes ![][3]
  * Otherwise it stays as ![][2]

## Keys
| Action | Keys |
| ------ | ---- |
| Toggle play/pause | `enter` |
| Paint at cursor | `space` |
| Clear grid | `x` |

## Cursor
| Action | Keys |
| ------ | ---- |
| Move cursor left | `←` or `h` |
| Move cursor down | `↓` or `j` |
| Move cursor up | `↑` or `k` |
| Move cursor right | `→` or `l` |

## Tools
| Action | Keys |
| ------ | ---- |
| Paint with ![][1] | `1` |
| Paint with ![][2] | `2` |
| Paint with ![][3] | `3` |
| Paint with ![][4] | `4` |

## Selection
| Action | Keys |
| ------ | ---- |
| Selection mode | `ctrl` |
| Copy selection | `y` |
| Paste selection | `p` |

## License
Distributed under the Eclipse Public License version 1.0.

[1]: resources/public/img/empty.png
[2]: resources/public/img/wire.png
[3]: resources/public/img/head.png
[4]: resources/public/img/tail.png

