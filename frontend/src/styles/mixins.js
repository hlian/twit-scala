export function clearFix() {
  return `
    zoom: 1;
    &:after, &:before {
      content: "";
      display: table;
    }
    &:after {
      clear: both;
    }
  `;
}

export function truncate() {
  return `
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  `;
}

export function absolutePosition() {
  return `
    position: absolute;
    top: 0;
    left: 0 ;
  `;
}
