import chokidar from 'chokidar';
import { spawn } from 'node:child_process';

const WATCH_PATHS = ['src', 'cypress', 'angular.json', '../DSW02-Practica01/src'];
const DEBOUNCE_MS = 1200;

let running = false;
let rerunRequested = false;
let timer;

function runSuite(reason) {
  if (running) {
    rerunRequested = true;
    return;
  }

  running = true;
  console.log(`\n[Cypress Watch] Running suite (${reason})...`);

  const child = spawn('npm', ['run', 'cypress:verify'], {
    stdio: 'inherit',
    shell: true
  });

  child.on('close', (code) => {
    running = false;
    console.log(`[Cypress Watch] Finished with exit code ${code}.`);

    if (rerunRequested) {
      rerunRequested = false;
      runSuite('queued changes');
    }
  });
}

const watcher = chokidar.watch(WATCH_PATHS, {
  ignoreInitial: true,
  awaitWriteFinish: {
    stabilityThreshold: 500,
    pollInterval: 100
  }
});

watcher.on('all', (event, path) => {
  clearTimeout(timer);
  timer = setTimeout(() => runSuite(`${event} ${path}`), DEBOUNCE_MS);
});

console.log('[Cypress Watch] Watching frontend and backend source changes');
runSuite('initial run');
