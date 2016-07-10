# Wireworld

A wireworld is made up of four types of cell.

* Empty ![][1]
* Wire ![][2]
* Electron Head ![][3]
* Electron Tail ![][4]

## Rules
* A ![][1] always stays ![][1]
* A ![][3] always becomes a ![][4]
* A ![][4] always becomes a ![2]
* If a ![][2] has 1 or 2 ![][3] neighbours, it becomes a ![][3]
* Otherwise it stays as a ![][2]

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
| Paint with `empty` | `1` |
| Paint with `wire` | `2` |
| Paint with `head` | `3` |
| Paint with `tail` | `4` |

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

